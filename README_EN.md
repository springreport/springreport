<p align="center">
	<img alt="logo" src="https://foruda.gitee.com/images/1718970127183749045/6c564279_14560165.png">
</p>
<h1 align="center" style="margin: 30px 0 30px; font-weight: bold;">Open-Source Enterprise-Level Report System</h1>

<p align="center">English | <a href="README.md">简体中文</a> </p>

## Table of Contents
- [Official Resources](#official-resources)
- [Project Description](#project-description)
- [Technical Architecture](#technical-architecture)
- [Core Advantages](#core-advantages)
- [Open Source / Paid Scope](#open-source--paid-scope)
- [Dependent Projects](#dependent-projects)
- [Database Support](#database-support)
- [Technical Support](#technical-support)
- [Copyright Notice](#copyright-notice)
- [Infringement Blacklist](#infringement-blacklist)
- [Donation Support](#donation-support)

## Official Resources
| Type       | Information                                                                 |
|------------|-----------------------------------------------------------------------------|
| Official Website       | [https://www.springreport.vip/](https://www.springreport.vip/)       |
| Project Documentation   | [https://www.springreport.vip/2.%E9%A1%B9%E7%9B%AE%E4%BB%8B%E7%BB%8D.html](https://www.springreport.vip/2.%E9%A1%B9%E7%9B%AE%E4%BB%8B%E7%BB%8D.html) |
| QQ Groups      | Group 1: 477055814 (Full)、Group 2: 1004742371 (Full)、Group 3: 1045719969      |
| Author's WeChat   | 986539100                                                            |

## Supporting Open Source
Maintaining an open-source project is no easy task. If this project helps you, please **give it a star** to support us — your appreciation means a lot! Thank you so much!

## Join the Community
If you'd like to communicate, learn and share, you're welcome to join our group!

## Commercial Use Notice
If you use this project for **company or commercial purposes**, please respect the author’s hard work.  
For commercial usage, please contact the author for authorization.  
This will help the project get sustainable maintenance and continuous updates!

## Project Description
SpringReport is an enterprise-level open-source report platform with zero-code drag-and-drop design. It provides one-stop support for Excel reports, Word templates, PPT, and data dashboards, compatible with mainstream databases and domestic independent innovation environments. Out-of-the-box, easy to integrate, and support secondary development, reducing report development costs by 90%.

The project started in 2020 (the first version CY-Report), was reconstructed based on luckysheet and renamed SpringReport in 2021, and has become stable after years of iteration. Due to the scarcity of open-source report systems and most of them being unmaintained, this project is open-sourced to provide a commercially usable, technically supported, and continuously updated report solution.

> **Note**: After downloading the project, please carefully read the README and all files (such as SQL scripts, dependent packages) to avoid local environment construction failures due to missing information.

## Technical Architecture
### Frontend
- Node v18.20.3
- Support Vue2 (ElementUI)、Vue3 (ElementPlus)
- Core Components: luckysheet、canvas-editor

### Backend
- SpringBoot 2.7.12 (Open Source) / 3.2.6 (Commercial)
- MyBatis-Plus
- Middleware: RocketMQ、Redis

## Core Advantages
1. Complies with the Apache2.0 protocol, commercially friendly, and supports secondary development;
2. Zero-code drag-and-drop designer for quickly making Chinese-style reports;
3. Online Excel multi-person collaborative editing with operation habits close to native Excel;
4. Support for displaying millions of levels of data and cell-level permission control;
5. Compatible with multiple databases (MySQL/Oracle/PostgreSQL/SqlServer/Dameng/Kingbase, etc.), adapted to independent innovation environments;
6. Report data sources support 13+ types (including InfluxDB, ClickHouse, Elasticsearch, etc.), with continuous expansion;
7. Support dynamic filling of Word templates and multi-tenant mode;
8. Independent research and development with independent intellectual property rights.

## Open Source / Paid Scope
### 1. Open Source Content
| Serial Number | Content                          | Open Source | Remarks                                   |
|------|-------------------------------|----------|----------------------------------------|
| 1    | SpringBoot2.7.12 backend code     | Yes       | -                                      |
| 2    | Vue2 frontend code                 | Yes       | -                                      |
| 3    | Vue3 frontend code                 | Yes       | -                                      |
| 4    | Database scripts                    | Partial     | MySQL is open source, other databases need to contact the author       |

### 2. Commercial Paid Content (Including Tax)
| Serial Number | Paid Content                                   | Price (CNY)     | Remarks                                                                 |
|------|--------------------------------------------|----------|----------------------------------------------------------------------|
| 1    | Remove watermark + Commercial authorization                        | ¥10499   | Only remove the watermark and provide commercial license, do not provide the source code of Luckysheet.|
| 2    | luckysheet optimized source code + Remove watermark + Commercial authorization    | ¥14999   | The Luckysheet source code is available. If you don't need to do secondary development on Luckysheet, purchasing the source code is unnecessary. Not having it does not affect usage at all.|
| 3    | SpringBoot3.2.6 backend code                   | ¥6499    | Functions are consistent with the SpringBoot2 version                                         |
| 4    | Multi-database support (Oracle/SqlServer/Dameng, etc.)    | ¥5499    | Refers to the underlying database for project deployment, not the report data source                                   |
| 5    | PPT plugin                                   | ¥6999    | Developed based on PPTist, backend is not open source                                         |
| 6    | OnlyOffice version Word designer                  | ¥5499    | Closed-source content                                                           |
| 7    | Custom function plugin                             | ¥6499    | Free for users who pay ≥¥12999                                                |
| 8    | Template export and import function                           | Gift     | Gift with any paid purchase (Original price ¥3499)                                     |
| 9    | Multi-dashboard function plugin                             | Gift     | Gift with any paid purchase (Original price ¥1999)                                     |
| 10   | Excel Report Snapshot plugin                          | Gift     | Gift with any paid purchase (Original price ¥1999)                                    |
| 11   | All content (Original price ¥51000+)                   | ¥29999   | Full source code delivery, permanent free upgrade, no limit on the number of projects                             |
| 12   | International version full source code                           | Quoted separately | Independent code, cannot be upgraded from the open-source version                                           |

> Details of the international version: [https://www.springreport.vip/7.%E5%9B%BD%E9%99%85%E5%8C%96%E7%89%88%E6%9C%AC%E8%AF%B4%E6%98%8E.html](https://www.springreport.vip/7.%E5%9B%BD%E9%99%85%E5%8C%96%E7%89%88%E6%9C%AC%E8%AF%B4%E6%98%8E.html)

## Dependent Projects
1. Excel to PDF toolkit `excel2pdf`：
   - GitHub：[https://github.com/springreport/excel2pdf](https://github.com/springreport/excel2pdf)
   - Gitee：[https://gitee.com/springreport/excel2pdf](https://gitee.com/springreport/excel2pdf)
   - GitCode：[https://gitcode.com/caiyangyang007/excel2pdf](https://gitcode.com/caiyangyang007/excel2pdf)
2. Some dependent Jar packages need to be manually imported (not in the Maven central repository): Obtain from the project's [SQL scripts and others] folder and put them into the local Maven repository.

## Database Support
| Serial Number | Database       | Supported | Script Provided |
|------|--------------|----------|--------------|
| 1    | MySQL 5.7+   | Yes       | Yes           |
| 2    | Oracle       | Yes       | Yes           |
| 3    | SqlServer    | Yes       | Yes           |
| 4    | PostgreSQL   | Yes       | Yes           |
| 5    | Dameng Database   | Yes       | Yes           |
| 6    | Kingbase     | Yes       | Yes           |
| 7    | Highgo Database   | Yes       | Yes           |
| 8    | vastbaseG100   | Yes       | Yes           |

## Technical Support
If you need technical support from the author, you can contact him through the WeChat group/QQ group. The author will charge a reasonable fee based on the time and energy spent, please understand.

## Copyright Notice
1. Use of the project must strictly comply with the Apache-2.0 open source agreement;
2. Do not remove the copyright watermark ("Welcome to use SpringReport") and Logo in the project without permission: retain them for free use, contact the author to purchase authorization for commercial use/removing watermark, and infringement will be held accountable (compensation starts from 20 times the pricing);
3. Aspose-Words relied on for Word to PDF conversion is a paid plugin. If commercial use is needed, please contact the official for authorization. The risk of using pirated versions at your own expense (you can also replace it with open source solutions such as Docx4j);
4. PPTist complies with the AGPL-3 protocol, closed-source commercial use needs to contact its author for authorization;
5. The OnlyOffice version Word designer uses the community version, and non-community versions need to contact OnlyOffice to purchase authorization by yourself.

## Infringement Blacklist
The following companies have infringed the use of SpringReport, please avoid risks when cooperating:

| Serial Number | Company Name               | Unified Social Credit Code       |
|------|------------------------|------------------------|
| 1    | 青岛艾瑞信息科技有限公司 | 91370203350278010W     |
| 2    | 杭州务新网络科技有限公司 | 91330108341851735A     |
| 3    | 深圳市牛童信息技术有限公司 | 91440300088334151A    |

## Donation Support
Open source is not easy. If SpringReport is helpful to you, you can support the research and development through the following donation methods:

<div align=center>
	<img src="https://www.springreport.vip/images/qrcode/alipay.jpg" width=300 height=300>
	<img src="https://www.springreport.vip/images/qrcode/wechat.jpg" width=300 height=300>
</div>

### Thanks to Sponsors
| Name         | Amount (CNY)  | Name           | Amount (CNY)  |
|--------------|-------|----------------|-------|
| AIfighting   | 50    | *扇            | 10    |
| LG           | 8.8   | *e             | 100   |
| 肖叮         | 50    | py__boy        | 260   |
| 君莫问       | 132   | *我            | 100   |
| 马*洁        | 500   | 老扫把头       | 200   |
| 超(yc3****3067) | 100 | *了            | 50    |
| 林*升        | 50    | **刚           | 188   |
| *刚(iql####fom) | 88  | c*e            | 8.88  |
| 再见不见     | 50    | 醉卧沙场君莫笑 | 66    |
| 心平气和     | 100   | null           | 100   |
| y.x.l        | 100   | *好            | 88    |
| A*n          | 20    | *静            | 15    |
| Meet and cherish | 100 | kirin、7       | 88.88 |
| *哒          | 15    | 朝花夕拾       | 30    |
| *句          | 30    | *海              | 100     |
| *p          | 20    |               |      |