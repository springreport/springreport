<p align="center">
	<img alt="logo" src="https://foruda.gitee.com/images/1718970127183749045/6c564279_14560165.png">
</p>
<h1 align="center" style="margin: 30px 0 30px; font-weight: bold;">开源的企业级报表系统</h1>

#### 官网

https://www.springreport.vip/
#### qq群

477055814
#### 作者微信

986539100
#### 使用文档

https://gitee.com/springreport/SpringReport/wikis/pages

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

1. SpringBoot2.7.12
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
10. 报表数据源目前支持多达11种：MySQL，Oracle，Postgresql，Sqlserver，influxdb，达梦数据库，人大金仓，clickhouse，elasticsearch，TDengine，http请求
   后续也会持续更新，支持更多的数据源
11. 支持多租户模式
12. 自主研发，拥有自主知识产权

## 依赖项目
SpringReport依赖另外一个工具类jar包excel2pdf，用于将excel转成pdf，可以从以下地址下载：

github地址：https://github.com/springreport/excel2pdf

gitee地址：https://gitee.com/springreport/excel2pdf

## 数据库支持
|序号   | 数据库  | 是否支持  | 是否提供脚本 |
|---|---|---|---|
| 1  | mysql5.7+  | 支持  | 提供  |
| 2  | oracle  | 支持  | 提供  |
| 3  | sqlserver  | 支持  | 提供  |
| 4  | postgresql  | 支持  | 提供  |
| 5  | 达梦数据库  | 支持  | 提供  |
| 6  | 人大金仓  | 支持  | 提供  |

## 技术支持
如果您需要作者的技术支持，请加入微信群或者QQ群联系作者。在这个过程中作者也需要付出时间和精力，所以也会适当的收取一些费用，还请理解。

## 捐赠SpringReport的研发
开源不易，如果你认为SpringReport项目可以为你提供帮助，或者给你带来方便和灵感，或者你认同这个项目，可以为我的付出赞助一下哦！
<div align=center>  
	<img src="https://www.springreport.vip/images/qrcode/alipay.jpg" width=300 height=300>  
	<img src="https://www.springreport.vip/images/qrcode/wechat.jpg" width=300 height=300>  
</div>
