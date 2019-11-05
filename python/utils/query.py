# -*- coding:utf8 -*-
import requests
import geoip2.database
from bs4 import BeautifulSoup
import sys
import io
import re


class Query:
    def getIp(self):
        req = requests.get("http://txt.go.sohu.com/ip/soip")
        ip = re.findall(r'\d+.\d+.\d+.\d+', str(req.content))
        return ip[0]

    def getOUIbymac(self, mac):
        maclist = mac.split(":")
        macstr = "-".join(maclist).strip()
        resp = requests.get("http://api.macvendors.com/"+macstr)
        if resp.status_code == 200:
            return resp.content

    def getInfobymac(self, mac):
        response = requests.post("http://wifi.tongxinmao.com/Public/macaddr", data=dict(mac=mac))
        if response.status_code == 200:
            soup = BeautifulSoup(response.content, "lxml")
            return soup.find(id="resText").text

    def getInfobyIp(self, ip):
        reader = geoip2.database.Reader("F:\\face\\map\\GeoLite2-City.mmdb")
        try:
            response = reader.city(ip)
            return dict(continent="{}({})".format(response.continent.names["es"], response.continent.names["zh-CN"]),
                        country="{}({})".format(response.country.name, response.country.names["zh-CN"]),
                        subdivisions="{}({})".format(response.subdivisions.most_specific.name,
                                                     response.subdivisions.most_specific.names["zh-CN"]),
                        city=response.city.name,
                        location=(response.location.longitude, response.location.latitude),
                        time_zone=response.location.time_zone,
                        ip=ip,
                        postalcode=response.postal.code)
        except Exception as e:
            pass


def main(argv):
    if argv[1] == "ip":
        print(q.getInfobyIp(argv[2]))
    elif argv[1] == "routeMac":
        print(q.getInfobymac(argv[2]))
    elif argv[1] == "macOUI":
        print(q.getOUIbymac(argv[2]))
    elif argv[1] == "selfip":
        print(q.getIp())


if __name__ == "__main__":
    q = Query()
    sys.stdout = io.TextIOWrapper(sys.stdout.buffer, encoding='utf8')
    main(sys.argv)
