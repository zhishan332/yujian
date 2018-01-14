from selenium.webdriver.support import expected_conditions as EC
from selenium.webdriver.common.by import By
from selenium import webdriver
from selenium.webdriver import DesiredCapabilities
from selenium.webdriver.common.keys import Keys
import requests
from bs4 import BeautifulSoup
import lxml
from PIL import Image
import os

from selenium.webdriver.support.wait import WebDriverWait

path = 'I:/doc/tutu/img/'

url = "https://qingbuyaohaixiu.com/archives/category/"

urls = []
urls.append(url + "美腿")
for num in range(2, 47):
    urls.append(url + "美腿/page/" + str(num))
urls.append(url + "好身材")
for num in range(2, 39):
    urls.append(url + "好身材/page/" + str(num))
urls.append(url + "在路上")
for num in range(2, 25):
    urls.append(url + "在路上/page/" + str(num))
urls.append(url + "大")
for num in range(2, 26):
    urls.append(url + "大/page/" + str(num))
urls.append(url + "贵在真实")
for num in range(2, 15):
    urls.append(url + "贵在真实/page/" + str(num))
urls.append(url + "美")
for num in range(2, 5):
    urls.append(url + "美/page/" + str(num))
urls.append(url + "趣图")
for num in range(2, 5):
    urls.append(url + "趣图/page/" + str(num))
urls.append(url + "害羞")
for num in range(2, 3):
    urls.append(url + "害羞/page/" + str(num))

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

driver1 = webdriver.PhantomJS(executable_path=r'I:\program\phantomjs-2.1.1-windows\bin\phantomjs.exe',
                              desired_capabilities=dcap,
                              service_args=["--ignore-ssl-errors=true", "--ssl-protocol=TLSv1"])
driver2 = webdriver.PhantomJS(executable_path=r'I:\program\phantomjs-2.1.1-windows\bin\phantomjs.exe',
                              desired_capabilities=dcap,
                              service_args=["--ignore-ssl-errors=true", "--ssl-protocol=TLSv1"])
# 设置10秒页面超时返回，类似于requests.get()的timeout选项，driver.get()没有timeout选项
# 以前遇到过driver.get(url)一直不返回，但也不报错的问题，这时程序会卡住，设置超时选项能解决这个问题。
driver1.set_page_load_timeout(60)
# 设置10秒脚本超时时间
driver1.set_script_timeout(60)
# 隐式等待5秒，可以自己调节
driver1.implicitly_wait(2)
driver2.set_page_load_timeout(60)
driver2.set_script_timeout(60)
driver2.implicitly_wait(2)

for url in urls:
    print("start geting....url:" + url)
    try:
        # driver1.set_window_size(1120, 550)
        driver1.get(url)
        # 等待页面渲染完成
        element = WebDriverWait(driver1, 60).until(EC.presence_of_element_located((By.ID, "loadedButton")))
    except Exception as e:
        print("msg:%s" % e)
        continue
    print("end geting....url:" + url)
    data = driver1.page_source
    soup = BeautifulSoup(data, "lxml")
    print("html:%s" % soup)
    page_url = soup.select(".entry-breaking h4 a")
    for i in page_url:
        z = i.get('href')
        if str('gif') in str(z):
            pass
        else:
            http_url = z
            try:
                driver2.get(http_url)
            except Exception as e:
                print("msg:%s" % e)
                continue
            data2 = driver2.page_source
            soup2 = BeautifulSoup(data2, "lxml")
            # print("html:%s" % soup2)
            img_url = soup2.select(".entry-content p img")
            for j in img_url:
                try:
                    sc = j.get('src')
                    r = requests.get(sc)
                    print('正在下载 %s......' % sc)
                    filePath = path + sc[-16:]
                    with open(filePath, 'wb')as jpg:
                        jpg.write(r.content)
                except Exception as e:
                    print("img download error,msg:%s" % e)
                    continue
            # print("http:%s" % z)
