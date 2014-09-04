# GameCenter/AppStore API 草案(9月4日更新)

## 概念

* **Namespace** 命名空间，可缩写为**NS**，代表是GameCenter或AppStore，可选的值为:
	* `app` AppStore
	* `game` GameCenter
	* `video` Videos

* **Item** 商店中售卖的商品，对应GameCenter中的Game，AppStore中的App

* **Album** 专辑，一组**Item**和**Album**的集合，有一些**Album**的ID是特定用途的:
	* `recommend` 推荐页(主页)
	* `categories` 所有**Category**的入口页
	* `{category}` 分类`category`中的所有**Item**集合
	* `{category}/{subcategory}` `category`中子分类`subcategory`种所有的**Item**集合

* **Type** 组织**Item**的一种方式，可选值为:
	* `album` 以专辑为视角的**Item**组织方式，是一个集合
	* `item` 以单个**Item**为视角，是单个**Item**

* **UI Type** UI类型，用于表达展示一组**Item**或一个**Item**的展示方法，例如
	* `metro` 使用类Metro布局展示
	* `detail` 展示一个**Item**的Detail

* **Sort** 观察一组**Item**的排序方式，可选值为:
	* `weighting` 综合排序（默认)
	* `created` 创建时间逆序
	* `updated` 更新时间逆序
	* `name` 名称正序
	* `score` 最佳
	* `download` 下载数

* **Image** 一张图片的URL及展现形式

* **Image Group** 多张图片组成的一个展现效果，分为背景图和覆盖图

* **Flags** 一组标志变量，用于表示**Item**的某种能力，例如:
	* `upgrade` App或Game是否可更新
	* `joystick`: Game是否支持游戏手柄


## 资源的URL模式

通用模式为`/{NS}/{Type}?id={ID}`，例如:
* `/game/album?id=album1_id` GameCenter中ID为`album1_id`的游戏专辑
* `/game/album?id=recommand` Game的主页内容
* `/game/album?id=action` 所有动作类游戏(`action`)的专辑
* `/game/item?id=game1_id` GameCenter中ID为`game1_id`的单个游戏

## 实体

实体是**Item**, **Album** 一种表达形式，使用**JSON**形式

* **规则**
	1. 使用`{Entity}`来表达引用一个实体的类型
	2. 在API调用时，指定`ui`参数，可使返回的Item具有不同的FieldSet


* `Image` 单张图片:

    ```
    {
        "url": "http://...", // 图片的URL
		"bgcolor": "#3Fde88", // 图片在绘制前需要绘制的背景色
        "pos": {"x":100, "y":300}, // (Optional) 图片的位置，如果为null则为坐标0,0
        "ani": { ... } // (Optional) 图片的动画效果，如果为null则表示无动画
        "size": {"w": 890, "h": 789} // 图片大小
    }
    ```

* `ImageGroup` 一组图片，有背景图和其他覆盖在其上面的图片

	```
    {
    	"bg": {Image}, // 背景图
        "text": {Image}, // 文字图
        "spirit": {Image}, // 精灵图
        "icon": {Image}, // 图标
        "thumb":{Image}, // 缩略图
        //...
    }
    ```

* `UI` 关于UI显示的相关信息

	```
    {
    	"type": "..."，// 当前UI的UI Type
        "layout": { // 适用于当前UI Type的布局信息，如不存在为null
        	"w": ..
            "h": ..
        }
    }
    ```

* `DisplayItem` 作为一个**Item**在列表中展示的基本Fields

	```
    {
    	"ns": "app|game|video", // Namespace
        "type": "category|album|item", // Type
        "id": "...", // 字符串形式的ID，对于Category来说，可能为空串
        "name": "..." // 名称
        "images": {ImageGroup}, // 图片
        "_ui": {UI}
    }
    ```

* `Item` **Item**完整公用信息(对于App或Game来说还有各自的信息)

	```
    // extends DisplayItem
    {
        "package": "...", // 应用包名
        "phrase": "...", // 一句话简介
        "vendor": { // 开发商
        	"id": "..."
        	"name": "北京春雨天下软件有限公司"
        },
        "flags": {
        	"upgrade": true
        }, // 标志
  		"description": "..." // 完整描述
        "times": {
        	"updated": 1909088888, // 更新时间
        },
        "ver": {
        	"code": "200", // 字符串形式的Version Code
            "name": "1.0.1" // 字符串形式的Version name
        },
        "sizes": {
        	"package": 1941662, // Byte单位的下载包大小
            "installed": 20098900, // Byte单位的安装后占用空间
        },
        "category": {
        	"id": "c1/c2", // 最为详细的分类路径
            "name": "...", // 所在分类名称
        },
        "score": 70, // 0-100的评分
        "screenshots": [
        	{ImageGroup},
            {ImageGroup},
            // ...
        ]
        "counts": {
        	"download": ... // 下载数量
          	// ...
        },
    }
    ```

* `Album` **Album**信息，包含有**Item**的列表

	```
    // extends DisplayItem
    {
    	"is_category": true|false，这个album是否是category
        "sort": "..", // sort 规则，如为Album返回则此值为null
        "pagi": "..", // 当前页码(基于1)
        "items": [
        	{DisplayItem},
            {DisplayItem},
            // ...
        ]
    }
    ```

* `AlbumList` 多页`Album`

	```
    {
    	"body": [{Album}, {Album}, ...]
        "preload": {
        	"images": [
            	"...",
                "..."
            ]
        }
    }
    ```
* `ItemList`

	```
    {
    	"body": [{Item}, {Item}],
        "preload": {
        	"images": [
            	"...",
                "..."
            ]
        }
    }
    ```

## API

### API公用信息

所有API都会附加下列参数，使用HTTP参数形式传递

* `locale` 区域代码，例如`zh_CN`
* `res` 分辨率，值为`hd720` | `hd1080` | `hd2160`
* `device`设备类型
* `device_id` 设备ID，一般为设备MAC地址的md5
* `ns_key` **Namespace**的Key
* `opaque` 利用`ns_key`和URI即参数算出的URL签名

### API 列表
* **GET** `/1/{ns}/category?id={id}&sort={sort}&page={page}&filter=...` 返回`{AlbumList}`
	获取**Category**内容
	1. `page`是基于1的，默认值为`1`
    1. `sort`参数对非**Category**类**Album**不起作用

* **GET** `/1/{ns}/album?id={id}`，返回: `{AlbumList}`
    获取**Album**内容

* **GET** `/1/{ns}/search?q=word&category=c1/c2`，返回: `{AlbumList}`
	搜索

* **GET** `/1/{ns}/home`，返回: `{AlbumList}`
	主页

* **GET** `/1/{ns}/item?id={id}`，返回: `{ItemList}`
	获取单个**Item**信息

* **GET** `/1/{ns}/download?item={id}`，返回: 待定
