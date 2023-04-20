### Git Actions

- github actions 생성
  - ./github/workflows/back.yml

### S3 -> EC2 배포
```yaml
name: Backend CI/CD

on:
  push:
    branches: [ "dev" ]

permissions:
  contents: read

env:
  PROFILE: dev
  APPLICATION_NAME: ${AWS Application Name}
  APPLICATION_GROUP_NAME: ${AWS Application Group Name}
  S3_BUCKET_NAME: ${AWS S3 Bucket Name}
  S3_BUCKET_DIRECTORY: ${AWS S3 Bucket Directory}
  ZIP_FILE: $APPLICATION_NAME.zip

jobs:
  backend:
    name: Backend CI/CD
    runs-on: ubuntu-latest
    defaults:
      run:
        shell: bash
        working-directory: ${Working Directory}      // The folder path where the build.gradle file is located

    steps:
      - uses: actions/checkout@v3

      - name: Set up JDK 11
        uses: actions/setup-java@v3
        with:
          java-version: '11'
          distribution: 'temurin'

      - name: Permission for gradlew
        run: chmod +x ./gradlew

      - name: Setup Gradle
        uses: gradle/gradle-build-action@v2
        with:
          gradle-version: 7.6.1

      - name: Execute Gradle build
        run: ./gradlew clean build -Pprofile=$PROFILE

      - name: Make zip file
        run: zip -r ./$ZIP_FILE .
        shell: bash

      - name: Configure AWS credentials
        uses: aws-actions/configure-aws-credentials@v1
        with:
          aws-access-key-id: ${{ secrets.AWS_ACCESS_KEY }}            // GITHUB PROJECT -> Settings -> Security -> Actions secrets and variables
          aws-secret-access-key: ${{ secrets.AWS_SECRET_KEY }}        // GITHUB PROJECT -> Settings -> Security -> Actions secrets and variables
          aws-region: ap-northeast-2

      - name: Upload to S3
        run: aws s3 cp --region ap-northeast-2 $ZIP_FILE s3://$S3_BUCKET_NAME/$S3_BUCKET_DIRECTORY/$ZIP_FILE

      - name: CodeDeploy
        run: >
          aws deploy create-deployment --application-name $APPLICATION_NAME
          --deployment-group-name $APPLICATION_GROUP_NAME
          --deployment-config-name CodeDeployDefault.AllAtOnce
          --s3-location bucket=$S3_BUCKET_NAME,bundleType=zip,key=$S3_BUCKET_DIRECTORY/$ZIP_FILE
```
