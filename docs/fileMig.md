# 매뉴얼 마이그레이션

## 파일 이관
- As-is :
/data/project/cstalk/apache/htdocs/static/images/v1images/

- To-be : 
/data/project/gasapp-cstalk/apache/data/attach/manual/[companyId]/[manualIndex]/

## 데이터 변경
- As-is :
/static/images/v1images/1.png

- To-be :
/manual/[companyId]/[manualIndex]/[pageNo].[ext]

```sql
UPDATE  manual 
SET     pdf_image_path = '/manual/1/1/' + replace(pdf_image_path, '/static/images/v1images/', '')
WHERE   pdf_image_path not null
```