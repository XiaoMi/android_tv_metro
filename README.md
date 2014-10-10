# TV Metro android lib and API
Design doc:
http://git.n.xiaomi.com/liuhuadong/tvmetro/blob/master/design/app_api.ppt

android tv metro

##1 TV Metro liberary and API
###android lib:
supply one framework to build sw540dp metro layout.

###API:
server API and data structure.

This framework help you easy to build one TV metro UI style applicaiton. As for detail business data definition, you need handle by your selft.

###Demo images:
<img src="http://git.n.xiaomi.com/liuhuadong/tvmetro/raw/master/design/main.png"/>
<img src="http://git.n.xiaomi.com/liuhuadong/tvmetro/raw/master/design/video.png"/>


##2，android Lib
RecommendCardView Card view
GenericSubjectLoader Loader for album(tabs is one instance of album)

##3，How to integrate the android lib?
you just need inherit MainActity and implement your Tabs Loader.
Refer to TVMetroSample application

##4, API and Data Structure for Metro display Item
<a href="https://raw.githubusercontent.com/AiAndroid/stream/master/tv/game/home.json">sample Home data</a>


Server API definition refer to:
https://raw.githubusercontent.com/AiAndroid/stream/master/tv/game/home.json

##Home display data
{
    "data": [
        {
            "items": [display items],
            "images": {},
            "name": "TAB 1",
            "id": "recommend",
            "type": "album",
            "ns": "video"
        },
        {
            "items": [display item],
            "images": {},
            "name": "TAB 2",
            "id": "recommend",
            "type": "album",
            "ns": "video"
        }
    ]
}

##Display Item:
{
    "target": {
        "type": "item"
    },
    "images": {
        "back": {
            "url": "",
            "ani": {},
            "pos": {}
        }
    },
    "name": "Display Name）",
    "times": {
        "updated": 1409202939,
        "created": 1409202939
    },
    "_ui": {
        "layout": {
            "y": 2,
            "x": 3,
            "w": 1,
            "h": 1
        },
        "type": "metro_cell_banner"
    },
    "id": "987722",
    "type": "item",
    "ns": "video"
}

How to implement your owver server?
step one:
1, define your home data
2, implement your details/list API
