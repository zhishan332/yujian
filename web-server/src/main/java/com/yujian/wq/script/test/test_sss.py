url="http://78.media.tumblr.com/f5e1be4669d9ece57f8ad3b82ac828b3/tumblr_p25hbbRUUc1vxatmqo1_1280.jpg"

import pycurl
from io import *
import requests

# c = pycurl.Curl()
# c.setopt(c.URL, url)
#
# buffer = BytesIO()
# c.setopt(c.WRITEDATA, buffer)
# c.setopt(pycurl.FOLLOWLOCATION, 1)
# c.setopt(pycurl.MAXREDIRS, 5)
# c.perform()
#
# buffer.getvalue().decode('iso-8859-1')

filePath="/Users/wangqing/Downloads/dddddddd.jpg"
proxies = {
  "http": "http://127.0.0.1:1087",
  "https": "https://127.0.0.1:1087",
}

r = requests.get(url,proxies=proxies)
# f = open(filePath, 'rb')
with open(filePath, 'wb')as jpg:
    jpg.write(r.content)