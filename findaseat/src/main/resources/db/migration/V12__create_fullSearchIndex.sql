CREATE index full_search_index_idx
ON tb_employees
USING GIN(
to_tsvector(
'portuguese', coalesce(employee_name, '')
	)
)