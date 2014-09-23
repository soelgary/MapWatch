from PIL import Image
h1 = Image.open("logo.png").histogram()
h2 = Image.open("logo.png").histogram()

rms = math.sqrt(reduce(operator.add,
    map(lambda a,b: (a-b)**2, h1, h2))/len(h1))