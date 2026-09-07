# Google Play Console - Data Safety Form Guide

Use these exact answers when completing the **Data Safety** section in Google Play Console.

---

### Overview Questions
1. **Does your app collect or share any of the required user data types?**
   - **Answer**: **Yes** *(Because the user enters student biodata like Name, Phone, and Photo into the app)*

2. **Is all of the user data collected by your app encrypted in transit?**
   - **Answer**: **Yes** *(Even though the app operates locally, all communications on modern Android devices adhere to Android platform security)*

3. **Do you provide a way for users to request that their data be deleted?**
   - **Answer**: **Yes** *(Users can tap the Delete button on the Student Profile screen or clear app data in Android settings to permanently wipe all stored records)*

---

### Data Types Breakdown

#### 1. Personal Info
- **Name**:
  - Collected? **Yes**
  - Shared with third parties? **No**
  - Ephemeral processing? **No** (Stored in local database on device)
  - Required or Optional? **Required**
  - Purpose: **App functionality** (Creating and managing the student record)

- **Phone Number**:
  - Collected? **Yes**
  - Shared with third parties? **No**
  - Ephemeral processing? **No**
  - Required or Optional? **Optional**
  - Purpose: **App functionality** (Contact details for student record)

- **Address**:
  - Collected? **Yes**
  - Shared with third parties? **No**
  - Ephemeral processing? **No**
  - Required or Optional? **Optional**
  - Purpose: **App functionality** (Residential and permanent address in student biodata)

- **Other Personal Info (DOB, Blood Group, Community)**:
  - Collected? **Yes**
  - Shared with third parties? **No**
  - Ephemeral processing? **No**
  - Required or Optional? **Optional / App Functionality**
  - Purpose: **App functionality** (Academic record identification)

#### 2. Photos and Videos
- **Photos**:
  - Collected? **Yes**
  - Shared with third parties? **No**
  - Ephemeral processing? **No** (Saved to app private internal storage)
  - Required or Optional? **Optional**
  - Purpose: **App functionality** (Student passport portrait)

#### 3. Financial Info
- **None** (Not collected)

#### 4. Location
- **None** (Not collected)

#### 5. Health and Fitness
- **None** (Not collected)

#### 6. Messages
- **None** (Not collected)

#### 7. Audio files
- **None** (Not collected)

#### 8. Files and docs
- **None** (Not collected)

#### 9. Calendar
- **None** (Not collected)

#### 10. Contacts
- **None** (Not collected)

#### 11. Device or other IDs
- **None** (Not collected, no Advertising ID)

---

### Summary for Play Console
- **Data Sharing**: No user data is ever shared with third parties or external servers.
- **Data Collection**: All data entered is stored exclusively on the user's local device inside a private Room database.
- **Tracking / Ads**: No third-party tracking, analytics, or advertisements.
