### Git Actions

- github actions 생성
  - ./github/workflows/front.yml

### CloudFront -> S3 배포 
```yaml
name: Frontend CI/CD

on:
  push:
    branches: ["dev"]

env:
  APPLICATION_NAME: ${AWS Application Name}
  APPLICATION_GROUP_NAME: ${AWS Application Group Name}
  S3_BUCKET_NAME: ${AWS S3 Bucket Name}

jobs:
  build:
    name: Frontend CI/CD
    runs-on: ubuntu-latest
    defaults:
      run:
        shell: bash
        working-directory: front
    strategy:
      matrix:
        node-version: [16.x]
    steps:
      - name: Checkout
        uses: actions/checkout@v2

      - name: Use Node.js ${{ matrix.node-version }}
        uses: actions/setup-node@v1
        with:
          node-version: ${{ matrix.node-version }}
          cache: 'npm'

      - name: Run npm install
        run: npm ci

      - name: Run npm run build
        run: npm run build --if-present

      - name: Configure AWS credentials
        uses: aws-actions/configure-aws-credentials@v1
        with:
          aws-access-key-id: ${{ secrets.AWS_ACCESS_KEY }}            // GITHUB PROJECT -> Settings -> Security -> Actions secrets and variables
          aws-secret-access-key: ${{ secrets.AWS_SECRET_KEY }}        // GITHUB PROJECT -> Settings -> Security -> Actions secrets and variables
          aws-region: ap-northeast-2

      - name: Upload to S3
        run: aws s3 sync out s3://$S3_BUCKET_NAME --acl public-read --delete
```
