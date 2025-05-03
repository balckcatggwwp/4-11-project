$(function() {
	// 開啟新增員工Modal時載入職稱和權限
	$('#addEmployeeModal').on('show.bs.modal', function() {
		// 載入職稱
		$.ajax({
			url: '/jobTitleApi/listByLoginUser',
			method: 'GET',
			success: function(data) {
				const $jobSelect = $('#jobTitleSelect');
				$jobSelect.empty();
				$jobSelect.append('<option value="">請選擇職稱</option>');
				data.forEach(job => {
					$jobSelect.append(`<option value="${job.jobTitleCategoryId}">${job.jobTitleName} (等級${job.jobLevel})</option>`);
				});
			},
			error: function() {
				Swal.fire('錯誤', '職稱資料讀取失敗', 'error');
			}
		});

		// 載入權限
		$.ajax({
			url: '/empPermissionApi/list',
			method: 'GET',
			success: function(data) {
				const $permContainer = $('#permissionsCheckboxes');
				$permContainer.empty();
				data.forEach(perm => {
					$permContainer.append(`
	                    <div class="form-check col-4">
	                        <input class="form-check-input" type="checkbox" value="${perm.empPermissionTypeId}" id="perm${perm.empPermissionTypeId}" name="permissions">
	                        <label class="form-check-label" for="perm${perm.empPermissionTypeId}">
	                            ${perm.empPermissionTypeName}
	                        </label>
	                    </div>
	                `);
				});
			},
			error: function() {
				Swal.fire('錯誤', '權限資料讀取失敗', 'error');
			}
		});
	});

	$('#addEmployeeForm').submit(function(e) {
		e.preventDefault();

		const password = $('#password').val();
		const password2 = $('#password2').val();

		if (password !== password2) {
			Swal.fire('密碼錯誤', '兩次輸入的密碼不一致', 'warning');
			return;
		}

		const selectedPermissions = [];
		$('input[name="permissions"]:checked').each(function() {
			selectedPermissions.push($(this).val());
		});

		const formData = {
			name: $('input[name="name"]').val(),
			gender: $('input[name="gender"]:checked').val(),
			nationalId: $('input[name="nationalId"]').val(),
			dateOfBirth: $('input[name="dateOfBirth"]').val(),
			entryTime: $('input[name="entryTime"]').val(),
			email: $('input[name="email"]').val(),
			phoneNumber: $('input[name="phoneNumber"]').val(),
			password: password,
			jobTitleCategoryId: $('#jobTitleSelect').val(),
			permissionIds: selectedPermissions
		};

		$.ajax({
			url: '/employeeApi/empRegister',
			method: 'POST',
			contentType: 'application/json',
			data: JSON.stringify(formData),
			success: function(res) {
				if (res.status === 'success') {
					Swal.fire('新增成功', '', 'success').then(() => {
						$('#addEmployeeModal').modal('hide');
						$('#employeeTable').DataTable().ajax.reload();
					});
				} else {
					Swal.fire('錯誤', res.message || '新增失敗', 'error');
				}
			},
			error: function() {
				Swal.fire('伺服器錯誤', '請稍後再試', 'error');
			}
		});
	});

});