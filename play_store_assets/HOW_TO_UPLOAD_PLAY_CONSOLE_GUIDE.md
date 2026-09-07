# Google Play Console Upload Guide (தமிழ் & English)

இந்த வழிகாட்டி மூலம் நீங்கள் உங்கள் **Student Record Manager** செயலியை Google Play Console-ல் மிக எளிதாக Upload செய்து வெளியிடலாம்.

---

## 📁 உங்கள் கோப்புறையில் உள்ள கோப்புகள் (Files in `/play_store_assets/`)

| File Name | Description | Purpose in Play Console |
| :--- | :--- | :--- |
| `icon_512x512.jpg` | 512×512 HD App Icon | **App Icon** (Main Store Listing) |
| `feature_graphic_1024x500.jpg` | 1024×500 Promotional Banner | **Feature Graphic** (Main Store Listing) |
| `screenshot_1_profile.jpg` | 9:16 Profile Screen Screenshot | **Phone Screenshots** (Store Listing) |
| `screenshot_2_form.jpg` | 9:16 Form Screen Screenshot | **Phone Screenshots** (Store Listing) |
| `APP_LISTING_METADATA.md` | Titles, Short & Full Descriptions | Copy-paste to **Store Listing Text** |
| `DATA_SAFETY_QUESTIONNAIRE.md` | Complete Data Safety Answers | Fill out **App Content > Data Safety** |
| `PRIVACY_POLICY.md` & `privacy_policy.html` | Privacy Policy Text & Webpage | Submit under **App Content > Privacy Policy** |

---

## 🚀 Step-by-Step Play Console Upload Procedure

### படி 1: AAB (Android App Bundle) உருவாக்குதல் (Generate App Bundle)
Google Play Store-க்கு APK-க்கு பதிலாக `.aab` கோப்பு தேவைப்படும்:
1. **AI Studio Settings Menu**: Top right menu > **Download APK / Bundle** > Generate Signed AAB / Export ZIP.
2. அல்லது Android Studio-வில்: `Build` > `Generate Signed Bundle / APK` > `Android App Bundle (.aab)`.

---

### படி 2: Play Console-ல் புதிய செயலியை உருவாக்குதல் (Create New App)
1. **Google Play Console** (https://play.google.com/console)-ல் உள்நுழையவும்.
2. **"Create app"** பொத்தானை அழுத்தவும்.
3. விவரங்களை உள்ளிடவும்:
   - **App name**: `Student Record Manager`
   - **Default language**: `English (United States) - en-US`
   - **App or game**: `App`
   - **Free or paid**: `Free`
   - **Declarations**: Google Play கொள்கைகளை ஏற்றுக்கொண்டு **"Create app"** அழுத்தவும்.

---

### படி 3: Main Store Listing அமைத்தல் (Store Listing Details)
இடது பக்க மெனுவில் **Grow** > **Store presence** > **Main store listing** செல்லவும்:
1. **App details**:
   - **App name**: `Student Record Manager` (22 எழுத்துகள்)
   - **Short description**: `APP_LISTING_METADATA.md`-ல் உள்ளதை Copy செய்து Paste செய்யவும்.
   - **Full description**: `APP_LISTING_METADATA.md`-ல் உள்ளதை Copy செய்து Paste செய்யவும்.
2. **Graphics**:
   - **App icon**: `play_store_assets/icon_512x512.jpg` கோப்பை Upload செய்யவும்.
   - **Feature graphic**: `play_store_assets/feature_graphic_1024x500.jpg` கோப்பை Upload செய்யவும்.
   - **Phone screenshots**: `screenshot_1_profile.jpg` மற்றும் `screenshot_2_form.jpg` கோப்புகளை Upload செய்யவும்.
3. கீழே உள்ள **"Save"** பொத்தானை அழுத்தவும்.

---

### படி 4: App Content & Policy கேள்விகளுக்கு பதிலளித்தல் (App Content)
இடது பக்க மெனுவில் **Policy and programs** > **App content** செல்லவும்:

1. **Privacy Policy**:
   - `privacy_policy.html`-ஐ GitHub Pages, Google Sites அல்லது வலைத்தளத்தில் பதிவேற்றி, அதன் URL-ஐ உள்ளிடவும்.
2. **Ads**:
   - **"No, my app does not contain ads"** என்பதைத் தேர்ந்தெடுக்கவும்.
3. **App Access**:
   - **"All functionality is available without special access"** என்பதைத் தேர்ந்தெடுக்கவும் (Login எதுவும் தேவையில்லை).
4. **Content Rating (IARC)**:
   - உங்கள் மின்னஞ்சல் முகவரியை (`siascfirstsem@gmail.com`) உள்ளிடவும்.
   - Category: **Utility, Productivity, Education or Other**.
   - வன்முறை அல்லது ஆட்சேபனைக்குரிய உள்ளடக்கம் இல்லை என்று அனைத்து கேள்விகளுக்கும் **"No"** என பதிலளிக்கவும்.
   - மதிப்பீடு: **Everyone / PEGI 3** கிடைக்கும்.
5. **Target Audience and Content**:
   - Target age: **18 and over**.
   - Could your store listing appeal to children? **No**.
6. **Data Safety**:
   - `DATA_SAFETY_QUESTIONNAIRE.md` கோப்பில் கொடுக்கப்பட்டுள்ள விடைகளை அப்படியே தேர்வு செய்யவும்.
   - Data collection: Personal Info (Name, Phone, Address), Photos (Profile picture).
   - Data sharing: **No data shared with third parties**.
   - Encryption in transit: **Yes**.
   - Data deletion: **Yes**.
7. **Government Apps**:
   - **"No"** (Unless this is officially an accredited state government agency app).
8. **Financial Features**:
   - **"My app does not provide any financial features"**.

---

### படி 5: Release உருவாக்குதல் (Create & Rollout Release)
1. இடது பக்க மெனுவில் **Release** > **Production** (அல்லது முதலில் **Testing > Closed testing**) செல்லவும்.
2. **"Create new release"** அழுத்தவும்.
3. உங்கள் **`.aab`** கோப்பை Drag & Drop செய்து Upload செய்யவும்.
4. **Release name**: `1.0.0 (Initial Release)` என கொடுக்கவும்.
5. **Release notes**:
   ```text
   Initial Release of Student Record Manager.
   - Digitize Student Academic Record Registers
   - Section A to D Biodata with automatic age calculator
   - Passport portrait photo integration
   - 100% offline and secure local database
   ```
6. **"Next"** > **"Save"** > **"Review release"** > **"Start rollout to Production"** அழுத்தவும்!

---

🎉 **வாழ்த்துக்கள்!** உங்கள் செயலி Google மதிப்பாய்வுக்குப் பிறகு Play Store-ல் நேரலையாக வெளியிடப்படும்!
