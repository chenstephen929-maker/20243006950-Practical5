# Practical 5 municipality summaries: sources and verification

The four summaries in `arrays.xml` are original English prose, retained exactly from the researched drafts except for Android string-resource escaping of apostrophes (`\'`). They appear in the required order: Beijing, Shanghai, Chongqing, Tianjin. The array resource is named `municipalities_array`.

Word counts use whitespace-separated words after removing Android escape backslashes. Hyphenated words count as one word. Titles, XML markup, comments, and source links are excluded.

| Array index | Municipality | Words |
| --- | --- | --- |
| 0 | Beijing | 97 |
| 1 | Shanghai | 100 |
| 2 | Chongqing | 100 |
| 3 | Tianjin | 97 |

## Beijing

- [Beijing Info](https://english.beijing.gov.cn/beijinginfo/index.html): capital status and national roles in politics, culture, international exchange, and scientific and technological innovation.
- [Demographic Geography](https://english.beijing.gov.cn/beijinginfo/facts/202006/t20200601_1912281.html): location at the northern edge of the North China Plain, surrounding mountains, and summer and winter climate.
- [Must-visit Sites](https://english.beijing.gov.cn/travellinginbeijing/mustvisitsites/): the Forbidden City, Summer Palace, and Temple of Heaven and their imperial associations.
- [Hutong](https://english.beijing.gov.cn/beijinginfo/culture/202005/t20200515_1898145.html): historic lanes and their cultural history.

The final sentence is an editorial synthesis of the city's historic monuments and present-day institutional roles; it adds no numerical or historical claim.

## Shanghai

- [Welcome to Shanghai](https://english.shanghai.gov.cn/en-Overview/20231212/c9aa0f22df154557bbbbb30d996eb2b2.html): municipal status, Yangtze River estuary and delta location, and financial and trade roles.
- [Shanghai Basic Facts 2021](https://english.shanghai.gov.cn/assets/Download/2021-ShanghaiBasicFacts.pdf): development as a major trading port in the nineteenth century and established roles in finance, trade, shipping, and innovation. Only stable historical and geographic facts were used from this older publication.
- [The Bund](https://english.shanghai.gov.cn/en-ScenicSpots/20231205/584672cc6d044eabb5f7f6fc9049a19f.html): Huangpu waterfront and historic buildings formerly occupied by financial institutions and trading companies. The portal credits Huangpu district government (`shhuangpu.gov.cn`).
- [Shanghai after dark: chapter seven](https://english.shanghai.gov.cn/en-PlanANightOut/20260813/7641656790524617b222204060989e54.html): the Lujiazui skyline across the Huangpu River.

The connection between the delta location, regional economy, and sea routes, and the final sentence about heritage and development, are clearly grounded editorial syntheses of these sources.

## Chongqing

- [Geography](https://english.cq.gov.cn/aboutchongqing/overview/Geography/): Southwest China location, Yangtze-Jialing confluence, mountainous and karst terrain, urban and agricultural areas, and central urban hub functions. The page credits Chongqing's local chronicles office.
- [Dazu Rock Carvings](https://english.cq.gov.cn/aboutchongqing/culture/CulturalHeritage/): cultural heritage and UNESCO World Heritage inscription on December 1, 1999.
- [Eat](https://english.cq.gov.cn/specials/Eat/): hot pot and Chongqing noodles as characteristic local foods.
- [Hongyadong Scenic Spot](https://english.cq.gov.cn/specials/explore/PhotoPerfectSpots/202608/t20260810_15912600.html): layered mountainside architecture, riverfront neighborhoods, and the meeting of the rivers.

## Tianjin

- [Historical Origins](https://en.tj.gov.cn/tianjininfo/profile/202312/t20231225_6489676.html): northern location, Haihe River and Bohai Sea, name referring to an imperial crossing, garrison established December 23, 1404, transportation history, and seaport function.
- [Advantageous location](https://en.tj.gov.cn/services/InvestinginTianjin/202312/t20231228_6493642.html): municipal status, proximity to Beijing, and Tianjin Port as a maritime outlet for northern inland regions. The portal credits `exploringtianjin.com`.
- [Exploring a city's spirit: Iconic prints and figurines await](https://en.tj.gov.cn/Updates/News/202607/t20260731_7345506.html): Yangliuqing New Year woodblock prints, Niren Zhang clay figurines, foreign-style buildings, and traditional crafts. This is first-hand China Daily cultural reporting republished by the official municipal portal; it is not municipally authored.

## Official website endpoints and verification limitations

Research and verification were performed on October 30, 2026. The suggested endpoints are official municipal English-language portals:

| Municipality | Intended official endpoint | Evidence of official identity and fetch result |
| --- | --- | --- |
| Beijing | https://english.beijing.gov.cn/ | The [About Us page](https://english.beijing.gov.cn/tabbar/ourwebsite/202005/t20200513_1896899.html) explicitly identifies the domain as the People's Government of Beijing Municipality's official portal. Indexed content and relevant deep pages were available; the root endpoint timed out through the research fetcher. |
| Shanghai | https://english.shanghai.gov.cn/ | The root successfully opened as SHANGHAI CHINA - International Services Shanghai; relevant city overview and cultural pages were available. |
| Chongqing | https://english.cq.gov.cn/ | The indexed [official homepage](https://english.cq.gov.cn/index.html) carries a Chongqing Municipal People's Government copyright notice. The research search index supplied relevant official page contents; direct root and index endpoint requests timed out through the fetcher. |
| Tianjin | https://en.tj.gov.cn/ | Relevant deep pages carry Tianjin Municipal People's Government copyright notices. The [Hong Kong SAR Government's Beijing Office](https://www.bjo.gov.hk/en/trade/doing_business/related_site.html) also identifies the endpoint as the Tianjin Municipal People's Government website. Deep pages opened; the root endpoint timed out through the research fetcher. |

Research-fetcher timeouts do not establish that an endpoint is broken in a normal browser. Official identity is verified, but this research did not confirm successful root loading for Beijing, Chongqing, and Tianjin on the Android device or emulator. Clicking Visit site for every municipality and confirming the destination in the external browser remains an application runtime check.
