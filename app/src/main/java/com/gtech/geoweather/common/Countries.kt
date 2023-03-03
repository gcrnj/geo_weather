package com.gtech.geoweather.common

import android.util.Log

object Countries {
    var countries = hashMapOf<String, String>()
        .apply {
            Log.d("Countries", "Applied")
            this["AD"] = "Andorra"
            this["AE"] = "United Arab Emirates"
            this["AF"] = "Afghanistan"
            this["AG"] = "Antigua and Barbuda"
            this["AI"] = "Anguilla"
            this["AL"] = "Albania"
            this["AM"] = "Armenia"
            this["AO"] = "Angola"
            this["AQ"] = "Antarctica"
            this["AR"] = "Argentina"
            this["AS"] = "American Samoa"
            this["AT"] = "Austria"
            this["AU"] = "Australia"
            this["AW"] = "Aruba"
            this["AX"] = "Aland Islands"
            this["AZ"] = "Azerbaijan"
            this["BA"] = "Bosnia and Herzegovina"
            this["BB"] = "Barbados"
            this["BD"] = "Bangladesh"
            this["BE"] = "Belgium"
            this["BF"] = "Burkina Faso"
            this["BG"] = "Bulgaria"
            this["BH"] = "Bahrain"
            this["BI"] = "Burundi"
            this["BJ"] = "Benin"
            this["BL"] = "Saint Barthélemy"
            this["BM"] = "Bermuda"
            this["BN"] = "Brunei Darussalam"
            this["BO"] = "Bolivia (Plurinational State of)"
            this["BQ"] = "Bonaire, Sint Eustatius and Saba"
            this["BR"] = "Brazil"
            this["BS"] = "Bahamas"
            this["BT"] = "Bhutan"
            this["BV"] = "Bouvet"
            this["BW"] = "Botswana"
            this["BY"] = "Belarus"
            this["BZ"] = "Belize"
            this["CA"] = "Canada"
            this["CC"] = "Cocos (Keeling) Islands"
            this["CD"] = "Congo (Democratic Republic of the)"
            this["CF"] = "Central African Republic"
            this["CG"] = "Congo"
            this["CH"] = "Switzerland"
            this["CI"] = "Côte d\'Ivoire"
            this["CK"] = "Cook Islands"
            this["CL"] = "Chile"
            this["CM"] = "Cameroon"
            this["CN"] = "China"
            this["CO"] = "Colombia"
            this["CR"] = "Costa Rica"
            this["CU"] = "Cuba"
            this["CV"] = "Cabo Verde"
            this["CW"] = "Curaçao"
            this["CX"] = "Christmas Island"
            this["CY"] = "Cyprus"
            this["CZ"] = "Czechia"
            this["DE"] = "Germany"
            this["DJ"] = "Djibouti"
            this["DK"] = "Denmark"
            this["DM"] = "Dominica"
            this["DO"] = "Dominican Republic"
            this["DZ"] = "Algeria"
            this["EC"] = "Ecuador"
            this["EE"] = "Estonia"
            this["EG"] = "Egypt"
            this["EH"] = "Western Sahara"
            this["ER"] = "Eritrea"
            this["ES"] = "Spain"
            this["ET"] = "Ethiopia"
            this["FI"] = "Finland"
            this["FJ"] = "Fiji"
            this["FK"] = "Falkland Islands (Malvinas)"
            this["FM"] = "Micronesia (Federated States of)"
            this["FO"] = "Faroe Islands"
            this["FR"] = "France"
            this["GA"] = "Gabon"
            this["GB"] = "United Kingdom"
            this["GD"] = "Grenada"
            this["GE"] = "Georgia"
            this["GF"] = "French Guiana"
            this["GG"] = "Guernsey"
            this["GH"] = "Ghana"
            this["GI"] = "Gibraltar"
            this["GL"] = "Greenland"
            this["GM"] = "Gambia"
            this["GN"] = "Guinea"
            this["GP"] = "Guadeloupe"
            this["GQ"] = "Equatorial Guinea"
            this["GR"] = "Greece"
            this["GS"] = "South Georgia and the South Sandwich Islands"
            this["GT"] = "Guatemala"
            this["GU"] = "Guam"
            this["GW"] = "Guinea-Bissau"
            this["GY"] = "Guyana"
            this["HK"] = "Hong Kong"
            this["HM"] = "Heard Island and McDonald Islands"
            this["HN"] = "Honduras"
            this["HR"] = "Croatia"
            this["HT"] = "Haiti"
            this["HU"] = "Hungary"
            this["ID"] = "Indonesia"
            this["IE"] = "Ireland"
            this["IL"] = "Israel"
            this["IM"] = "Isle of Man"
            this["IN"] = "India"
            this["IO"] = "British Indian Ocean Territory"
            this["IQ"] = "Iraq"
            this["IR"] = "Iran"
            this["IS"] = "Iceland"
            this["IT"] = "Italy"
            this["JE"] = "Jersey"
            this["JM"] = "Jamaica"
            this["JO"] = "Jordan"
            this["JP"] = "Japan"
            this["KE"] = "Kenya"
            this["KG"] = "Kyrgyzstan"
            this["KH"] = "Cambodia"
            this["KI"] = "Kiribati"
            this["KM"] = "Comoros"
            this["KN"] = "Saint Kitts and Nevis"
            this["KP"] = "North Korea"
            this["KR"] = "South Korea"
            this["KW"] = "Kuwait"
            this["KY"] = "Cayman Islands"
            this["KZ"] = "Kazakhstan"
            this["LA"] = "Laos"
            this["LB"] = "Lebanon"
            this["LC"] = "Saint Lucia"
            this["LI"] = "Liechtenstein"
            this["LK"] = "Sri Lanka"
            this["LR"] = "Liberia"
            this["LS"] = "Lesotho"
            this["LT"] = "Lithuania"
            this["LU"] = "Luxembourg"
            this["LV"] = "Latvia"
            this["LY"] = "Libya"
            this["MA"] = "Morocco"
            this["MC"] = "Monaco"
            this["MD"] = "Moldova"
            this["ME"] = "Montenegro"
            this["MF"] = "Saint Martin (French part)"
            this["MG"] = "Madagascar"
            this["MH"] = "Marshall Islands"
            this["MK"] = "North Macedonia"
            this["ML"] = "Mali"
            this["MM"] = "Myanmar"
            this["MN"] = "Mongolia"
            this["MO"] = "Macao"
            this["MP"] = "Northern Mariana Islands"
            this["MQ"] = "Martinique"
            this["MR"] = "Mauritania"
            this["MS"] = "Montserrat"
            this["MT"] = "Malta"
            this["MU"] = "Mauritius"
            this["MV"] = "Maldives"
            this["MW"] = "Malawi"
            this["MX"] = "Mexico"
            this["MY"] = "Malaysia"
            this["MZ"] = "Mozambique"
            this["NA"] = "Namibia"
            this["NC"] = "New Caledonia"
            this["NE"] = "Niger"
            this["NF"] = "Norfolk Island"
            this["NG"] = "Nigeria"
            this["NI"] = "Nicaragua"
            this["NL"] = "Netherlands"
            this["NO"] = "Norway"
            this["NP"] = "Nepal"
            this["NR"] = "Nauru"
            this["NU"] = "Niue"
            this["NZ"] = "New Zealand"
            this["OM"] = "Oman"
            this["PA"] = "Panama"
            this["PE"] = "Peru"
            this["PF"] = "French Polynesia"
            this["PG"] = "Papua New Guinea"
            this["PH"] = "Philippines"
            this["PK"] = "Pakistan"
            this["PL"] = "Poland"
            this["PM"] = "Saint Pierre and Miquelon"
            this["PN"] = "Pitcairn"
            this["PR"] = "Puerto Rico"
            this["PS"] = "Palestine, State of"
            this["PT"] = "Portugal"
            this["PW"] = "Palau"
            this["PY"] = "Paraguay"
            this["QA"] = "Qatar"
            this["RE"] = "Réunion"
            this["RO"] = "Romania"
            this["RS"] = "Serbia"
            this["RU"] = "Russia"
            this["RW"] = "Rwanda"
            this["SA"] = "Saudi Arabia"
            this["SB"] = "Solomon Islands"
            this["SC"] = "Seychelles"
            this["SD"] = "Sudan"
            this["SE"] = "Sweden"
            this["SG"] = "Singapore"
            this["SH"] = "Saint Helena, Ascension and Tristan da Cunha"
            this["SI"] = "Slovenia"
            this["SJ"] = "Svalbard and Jan Mayen"
            this["SK"] = "Slovakia"
            this["SL"] = "Sierra Leone"
            this["SM"] = "San Marino"
            this["SN"] = "Senegal"
            this["SO"] = "Somalia"
            this["SR"] = "Suriname"
            this["SS"] = "South Sudan"
            this["ST"] = "Sao Tome and Principe"
            this["SV"] = "El Salvador"
            this["SX"] = "Sint Maarten (Dutch part)"
            this["SY"] = "Syrian Arab Republic"
            this["SZ"] = "Eswatini"
            this["TC"] = "Turks and Caicos Islands"
            this["TD"] = "Chad"
            this["TF"] = "French Southern Territories"
            this["TG"] = "Togo"
            this["TH"] = "Thailand"
            this["TJ"] = "Tajikistan"
            this["TK"] = "Tokelau"
            this["TL"] = "Timor-Leste"
            this["TM"] = "Turkmenistan"
            this["TN"] = "Tunisia"
            this["TO"] = "Tonga"
            this["TR"] = "Turkey"
            this["TT"] = "Trinidad and Tobago"
            this["TV"] = "Tuvalu"
            this["TW"] = "Taiwan, Province of China"
            this["TZ"] = "Tanzania, United Republic of"
            this["UA"] = "Ukraine"
            this["UG"] = "Uganda"
            this["UM"] = "United States Minor Outlying Islands"
            this["US"] = "United States of America"
            this["UY"] = "Uruguay"
            this["UZ"] = "Uzbekistan"
            this["VA"] = "Holy See"
            this["VC"] = "Saint Vincent and the Grenadines"
            this["VE"] = "Venezuela (Bolivarian Republic of)"
            this["VG"] = "Virgin Islands (British)"
            this["VI"] = "Virgin Islands (U.S.)"
            this["VN"] = "Viet Nam"
            this["VU"] = "Vanuatu"
            this["WF"] = "Wallis and Futuna"
            this["WS"] = "Samoa"
            this["YE"] = "Yemen"
            this["YT"] = "Mayotte"
            this["ZA"] = "South Africa"
            this["ZM"] = "Zambia"
            this["ZW"] = "Zimbabwe"
        }
        private set
}