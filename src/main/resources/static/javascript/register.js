const csrfHeader = document.querySelector('meta[name="_csrf_header"]').content;
       const csrfToken = document.querySelector('meta[name="_csrf"]').content;

       const validateField = (field, regex, errorField, errorMessage) => {
           field.addEventListener('input', () => {
               if (regex.test(field.value.trim())) {
                   field.classList.add('valid');
                   field.classList.remove('invalid');
                   errorField.style.display = 'none';
               } else {
                   field.classList.add('invalid');
                   field.classList.remove('valid');
                   errorField.textContent = errorMessage;
                   errorField.style.display = 'block';
               }
           });
       };

       // 即時檢查用戶名
       const nameField = document.getElementById('mebName');
       const nameError = document.getElementById('mebNameError');
       validateField(nameField, /^.{2,20}$/, nameError, "用戶名長度必須在 2 到 20 字之間");

       // 即時檢查密碼
       const passwordField = document.getElementById('mebPwd');
       const passwordError = document.getElementById('mebPwdError');
       validateField(passwordField, /^(?=.*[a-zA-Z])(?=.*\d).{6,16}$/, passwordError, "密碼必須包含字母和數字，長度為 6~16");

       // 即時檢查電子郵件
       const emailField = document.getElementById('mebMail');
       const emailError = document.getElementById('mebMailError');
       const emailSuccess = document.getElementById('mebMailSuccess');
       emailField.addEventListener('input', () => {
           const email = emailField.value.trim();
           if (email === "") {
               emailError.textContent = "電子郵件不可為空";
               emailError.style.display = 'block';
               emailSuccess.style.display = 'none';
               emailField.classList.add('invalid');
               emailField.classList.remove('valid');
               return;
           }

           fetch('/front_end/checkEmail', {
               method: 'POST',
               headers: {
                   'Content-Type': 'application/json',
                   [csrfHeader]: csrfToken
               },
               body: JSON.stringify({ email }),
           })
               .then(response => response.json())
               .then(data => {
                   if (data.exists) {
                       emailError.textContent = "電子郵件已被註冊";
                       emailError.style.display = 'block';
                       emailSuccess.style.display = 'none';
                       emailField.classList.add('invalid');
                       emailField.classList.remove('valid');
                   } else {
                       emailError.style.display = 'none';
                       emailSuccess.style.display = 'block';
                       emailField.classList.add('valid');
                       emailField.classList.remove('invalid');
                   }
               });
       });