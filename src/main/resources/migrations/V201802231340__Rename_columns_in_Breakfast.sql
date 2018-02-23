-- rename foreign keys
EXEC sp_RENAME 'BreakfastItem.breakfast_category_id' , 'category_id', 'COLUMN'
EXEC sp_RENAME 'BreakfastCategory.breakfast_id' , 'service_id', 'COLUMN'
EXEC sp_RENAME 'audit.BreakfastCategory_BreakfastItem_AUD.breakfast_category_id' , 'category_id', 'COLUMN'
EXEC sp_RENAME 'audit.Breakfast_BreakfastCategory_AUD.breakfast_id' , 'service_id', 'COLUMN'

-- rename a column in BreakfastCategory
EXEC sp_RENAME 'BreakfastCategory.category' , 'name', 'COLUMN'
EXEC sp_RENAME 'audit.BreakfastCategory_AUD.category' , 'name', 'COLUMN'