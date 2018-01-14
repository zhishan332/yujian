from selenium import webdriver
from selenium.webdriver import DesiredCapabilities
from selenium.webdriver.common.keys import Keys
import requests
from bs4 import BeautifulSoup
import lxml
from PIL import Image
import os

path = 'I:/doc/tutu/img/'

urls = ["http://jandan.net/ooxx/page-{}#comments".format(str(i)) for i in range(262, 300)]
img_url = []
dcap = dict(DesiredCapabilities.PHANTOMJS)
# 从USER_AGENTS列表中随机选一个浏览器头，伪装浏览器
dcap["phantomjs.page.settings.userAgent"] = (
    "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/51.0.2704.63 Safari/537.36")
#
# dcap["phantomjs.page.settings.userAgent"] = (
#     "Mozilla/5.0 (Macintosh; Intel Mac OS X 10.9; rv:25.0) Gecko/20100101 Firefox/25.0 "
# )
# 不载入图片，爬页面速度会快很多
# dcap["phantomjs.page.settings.loadImages"] = False

driver = webdriver.PhantomJS(executable_path=r'I:\program\phantomjs-2.1.1-windows\bin\phantomjs.exe',
                             desired_capabilities=dcap,
                             service_args=["--ignore-ssl-errors=true", "--ssl-protocol=TLSv1"])
# 设置10秒页面超时返回，类似于requests.get()的timeout选项，driver.get()没有timeout选项
# 以前遇到过driver.get(url)一直不返回，但也不报错的问题，这时程序会卡住，设置超时选项能解决这个问题。
driver.set_page_load_timeout(10)
# 设置10秒脚本超时时间
driver.set_script_timeout(10)
# 隐式等待5秒，可以自己调节
driver.implicitly_wait(2)
for url in urls:
    print("start geting....url:" + url)
    try:
        driver.get(url)
    except Exception as e:
        print("msg:%s" % e)
        continue
    print("end geting....url:" + url)
    data = driver.page_source
    soup = BeautifulSoup(data, "lxml")
    images = soup.select("a.view_img_link")

    for i in images:
        z = i.get('href')
        if str('gif') in str(z):
            pass
        else:
            http_url = "http:" + z
            img_url.append(http_url)
            # print("http:%s" % z)

    for j in img_url:
        try:
            r = requests.get(j)
        except Exception as e:
            print("img download error,msg:%s" % e)
            continue
        print('正在下载 %s......' % j)
        filePath = path + j[-20:]
        with open(filePath, 'wb')as jpg:
            jpg.write(r.content)

