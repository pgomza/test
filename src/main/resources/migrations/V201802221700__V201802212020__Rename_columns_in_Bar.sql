-- rename foreign keys
EXEC sp_RENAME 'BarItem.bar_category_id' , 'category_id', 'COLUMN'
EXEC sp_RENAME 'BarCategory.bar_id' , 'service_id', 'COLUMN'
EXEC sp_RENAME 'audit.BarCategory_BarItem_AUD.bar_category_id' , 'category_id', 'COLUMN'
EXEC sp_RENAME 'audit.Bar_BarCategory_AUD.bar_id' , 'service_id', 'COLUMN'

-- rename a column in BarCategory
EXEC sp_RENAME 'BarCategory.category' , 'name', 'COLUMN'
EXEC sp_RENAME 'audit.BarCategory_AUD.category' , 'name', 'COLUMN'
