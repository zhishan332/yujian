from PIL import Image
import os

path = 'I:/doc/tutu/img/2/'

for file in os.listdir(path):

    checkfile = os.path.join(path, file)
    fp = open(checkfile, 'rb')
    img = Image.open(fp)
    x, y = img.size
    fp.close()
    if x < 500 or y < 600:
        os.remove(checkfile)
        print("remove file %s" % checkfile)
