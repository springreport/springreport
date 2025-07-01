<p align="center">
	<img alt="logo" src="https://foruda.gitee.com/images/1718970127183749045/6c564279_14560165.png">
</p>
<h1 align="center" style="margin: 30px 0 30px; font-weight: bold;">开源的企业级报表系统</h1>

#### 官网

https://www.springreport.vip/
#### qq群

群一：477055814(已满)      群二：1004742371(已满)     群三：1045719969
#### 作者微信

986539100
#### 项目文档

https://gitee.com/springreport/springreport/wikis/pages

## 开源不易，如果项目对你有帮助请给个star支持一下！十分感谢！

## 开源不易，如果想交流学习，欢迎加群交流！

## 开源不易，如果公司或者涉及到商业使用，希望尊重作者的劳动和付出，付费的项目请积极联系作者授权！这样也利于项目的可持续维护更新！

## 注意事项
 **很多人下载下来项目后，只关注代码，其他内容并没有关注，导致有些内容找不到，比如说sql脚本，依赖的一些其他项目或者jar包等等。
不管是下载之前还是下载之后，请一定要仔细阅读readme，然后下载下来的都有哪些内容也都需要看一下，不要只看代码，导致自己本地环境搭建有问题。
阅读readme和了解下载下来的都有哪些东西是玩转开源项目的基础，请大家一定要注意。**

## 系统简介
SpringReport是一款企业级的报表系统，支持在线设计报表，并绑定动态数据源，无需写代码即可快速生成想要的报表，可以支持excel报表和word报表两种格式，同时还可以支持excel多人协同编辑，后续考虑实现大屏设计器功能，通过简单的设计器可以生成炫酷的大屏效果。

SpringReport项目是从2021年正式开始开发，从2020年就有第一个版本，叫CY-Report，后来发现有luckysheet这个组件，开始正式使用luckysheet进行开发，并将项目命名为SpringReport并重新进行开发，经过这几年的不断完善，项目功能也越来越完善，也趋于稳定，并且在这几年对报表系统的研究过程中，发现开源的报表系统确实是太少了，就算开源也基本不维护了，遂决定将SpringReport开源，提供一套好用的，有技术支持的，有维护更新的报表系统。

## 技术架构
前端：

1. node v18.20.3
1. 支持vue2和vue3
1. vue2+ElementUi  vue3+ElementPlus
1. luckysheet  canvas-editor

后端：

1. SpringBoot2.7.12/SpringBoot3.2.6
1. mybatis-plus
1. RocketMQ；Redis

## 为什么选择SpringReport

1. 开源项目，遵循apache2.0开源协议，对商用友好，拿来即用，也便于进行二次开发
2. 拖拽式报表设计器，无需写代码，快到几分钟即可做出自己想要的中国式报表
3. 在线excel协同文档，支持多人协同编辑
4. 符合接近excel的操作习惯
5. 支持百万级别的数据展示
6. 精细到单元格级别的权限控制
7. 支持word模板设计，word模板动态绑定数据进行填充
8. 兼容支持的数据库有MySQL/Oracle/Postgresql/SqlServer/达梦数据库/人大金仓，不管是否有信创要求都可以支持
9. Springboot+vue前后端分离技术架构，vue2和vue3都可支持
10. 报表数据源目前支持多达13种：MySQL，Oracle，Postgresql，Sqlserver，influxdb，达梦数据库，人大金仓，clickhouse，elasticsearch，TDengine，http请求，瀚高数据库，doris数据库，
   后续也会持续更新，支持更多的数据源
11. 支持多租户模式
12. 自主研发，拥有自主知识产权

## 开源范围：
|序号   | 内容  | 是否开源  | 备注 |
|---|---|---|---|
| 1  | SpringBoot2.7.12版本后台代码  | 开源  |   |
| 2  | vue2版本前端代码  | 开源  |   |
| 3  | vue3版本前端代码  | 开源  |   |
| 4  | 数据库  | mysql开源，其余版本数据库不开源 | 需要支持其他版本的数据库请与作者联系,此处的数据库指的是项目本身部署所支持的数据库，并不是报表数据源只支持mysql，报表数据源支持的数据库没有限制  |
| 5  | SpringBoot3.2.6版本后台代码  | 不开源  | 需要请与作者联系  |
| 6  | PPT插件  | 后端不开源  | 需要请与作者联系  |
| 7  | onlyoffice版本word设计器  | 不开源  | 需要请与作者联系  |

### 为了项目的可持续发展，目前有以下几部分内容是收费的，收费内容都是统一价格，没有优惠，还请理解
### 注：付费部分只是提供源码，如果没有源码也完全不会影响使用，根据自己的需要选择是否购买即可
| 序号  | 收费内容  | 价格  | 备注  |
|---|---|---|---|
| 1 | 去除水印+商用授权  | ¥7499(含税)  | 只是去掉水印，不提供以下内容的源代码，如果对水印没有要求的可以忽略 |
| 2 | luckysheet优化后的源代码+去除水印+商用授权   | ¥12999(含税)  | 如果不需要对luckysheet进行二次开发的话没必要购买源码，没有源码对使用完全没有影响 |
| 3 | SpringBoot3框架对应的源代码 | ¥6499(含税)  | 功能上与SpringBoot2版本是一样的，只是框架上的区别 |
| 4 | 对oracle,sqlserver,postgresql,达梦，人大金仓数据库，瀚高数据库的支持| ¥5499(含税)  | 此处的数据库指的是项目底层的数据库支持，并不是报表数据源的数据库支持，报表数据源没有限制 |
| 5 | PPT插件| ¥6999(含税)  | PPT插件使用的是前端开源项目PPTist，后端不对外开放，如果需要请与作者联系 |
| 6 | onlyoffice版本word设计器| ¥5499(含税)  | onlyoffice版本word设计器属于不开源内容，如果需要请与作者联系 |
| 7 | 以上全部内容| ¥27999(含税)  |全部源码交付，永久免费升级，没有项目数量限制 |

## 依赖项目
SpringReport依赖另外一个工具类jar包excel2pdf，用于将excel转成pdf，可以从以下地址下载：

github地址：https://github.com/springreport/excel2pdf

gitee地址：https://gitee.com/springreport/excel2pdf

gitcode地址：https://gitcode.com/caiyangyang007/excel2pdf

SpringReport还有部分依赖的jar包无法直接通过pom.xml下载，需要单独引入放到自己本地的maven库，这些jar在git中都有，从【sql脚本和其他】文件夹中获取

## 数据库支持
|序号   | 数据库  | 是否支持  | 是否提供脚本 |
|---|---|---|---|
| 1  | mysql5.7+  | 支持  | 提供  |
| 2  | oracle  | 支持  | 提供  |
| 3  | sqlserver  | 支持  | 提供  |
| 4  | postgresql  | 支持  | 提供  |
| 5  | 达梦数据库  | 支持  | 提供  |
| 6  | 人大金仓  | 支持  | 提供  |
| 7  | 瀚高数据库  | 支持  | 提供  |

## 技术支持
如果您需要作者的技术支持，请加入微信群或者QQ群联系作者。在这个过程中作者也需要付出时间和精力，所以也会适当的收取一些费用，还请理解。

## 版权声明
1、使用该项目的同时请严格遵守apache-2.0开源协议。

2、对于项目中添加的版权信息水印（欢迎使用SpringReport）和SpringReport的logo信息，请勿擅自去掉，这些信息只是保护作者版权信息，并不会影响实际使用。如要免费使用需要保留该水印和logo，如需要去掉水印和logo使用(不管是商用还是公司内部使用)请联系作者购买授权，
   未取得作者授权擅自去掉的，作者将依法追究相关责任并赔偿作者相关经济损失(定价的20倍起步)。
   
3、 word生成pdf使用的是aspose-words插件，该插件是付费插件，如果要使用此方案请联系aspose官方付费使用，当然就想免费盗版使用那也没问题，出现问题需要自行承担。
如果不想使用该方案，可以使用其他开源方案，例如Docx4j等

4、PPTist是开源项目，开源协议是AGPL-3协议，使用时请严格遵守AGPL-3协议，请勿闭源商用，如需闭源商用请联系PPTist作者购买授权。

5、OnlyOffice版本的word设计器使用的是社区版的OnlyOffice，如果需要使用非社区版，请自行联系OnlyOffice购买授权。

## 捐赠SpringReport的研发
开源不易，如果你认为SpringReport项目可以为你提供帮助，或者给你带来方便和灵感，或者你认同这个项目，可以为我的付出赞助一下哦！
<div align=center>  
	<img src="https://www.springreport.vip/images/qrcode/alipay.jpg" width=300 height=300>  
	<img src="https://www.springreport.vip/images/qrcode/wechat.jpg" width=300 height=300>  
</div>

## 感谢赞助
|姓名   | 金额  |姓名   | 金额  |
|---|---|---|---|
| AIfighting  | 50  | *扇  | 10  |
| LG  | 8.8  | *e  | 100  |
| 肖叮  | 50  | py__boy  | 260  |
| 君莫问  | 66*2  | *我  | 100  |
| 马*洁  | 500  | 老扫把头  | 200  |
| 超(yc3****3067)  | 100  | *了  | 50  |
| 林*升  | 50  | **刚  | 188  |
| *刚(iql####fom)  | 88  | c*e  | 8.88  |
| 再见不见 | 50  | 醉卧沙场君莫笑  | 66  |
| 心平气和 | 100  | null  | 100  |
| y.x.l | 100  | *好  | 88  |
| A*n | 20  | *静  | 15  |
| Meet and cherish | 100  |
