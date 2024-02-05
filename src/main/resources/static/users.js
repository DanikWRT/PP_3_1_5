//Таблица AllUsers

const tbody = document.querySelector("#tableBody");                                //возвращает все элементы
const tableOfUsers = document.querySelector("#tableAllUsers");
let allUsersTable = "";
$(document).ready(async function () {  // запускает код, когда ДОМ становится безопасной для работв, эта функция возвращает промис
    let users = await fetch("http://localhost:8080/api/users").then(r => r.json());   // заставит ждать до тех пор, пока промис не выполнится
    const template = document.querySelector('#product');
    users.forEach((u) => {
        let rol = "";
        u.roles.forEach((r) => {
            rol += r.withoutPrefix + "  ";
        });
        allUsersTable += `<tr id="dele${u.id}"><td>${u.id}</td> 
                                            <td>${u.username}</td>
                                            <td>${u.email}</td>
                                            <td>${u.firstName}</td>
                                            <td>${u.lastName}</td>
                                            <td style="display:none;">${u.password}</td>
                                            <td>${rol}</td>
                                            <td><button class="btn btn-info editbtn" >Edit</button></td>
                                            <td><button class="btn btn-danger delbtn">Delete</button></td></tr>`;
    })
    tbody.innerHTML = allUsersTable;    // содержимое html в виде строки
})


// Список ролей в форму создания пользователя
$(document).ready(async function () {
    let rolles = await fetch("http://localhost:8080/api/roles").then(r => r.json());         //настройки для fetch можно указать после url
    let rol = "";
    rolles.forEach((r) => rol += `<option value="${r.name}">${r.withoutPrefix}</option>`);
    let sel = `<select name="sele" onchange="console.log($('#select').val())" id="select"  
                        multiple class="form-control sel" size="3">${rol}</select>`;
    document.getElementById('selector').innerHTML = sel;      // получение по id
})

//Список ролей в форму изменения пользователей

async function rol(userRol) {
    console.log(userRol);
    let rolles = await fetch("http://localhost:8080/api/roles").then(r => r.json());
    let rol = "";
    for (let n = 0; n < rolles.length; n++) {
        rol += `<option value="${rolles[n].name}">${rolles[n].withoutPrefix}</option>`;
    }
    let sel = `<select name="sele" onchange="console.log($('#selectEdit').val())" id="selectEdit"  
                        multiple class="form-control sel" size="3">${rol}</select>`;
    document.getElementById('selectorEdit').innerHTML = sel;
}


// доступ на нужную страницу по роли принципала
$(document).ready(async function () {
    let principal = await fetch("http://localhost:8080/api/info").then(r => r.json());
    for (let i = 0; i < principal.roles.length; i++) {
        if (principal.roles[i].name === 'ROLE_ADMIN') {
            return;
        }
    }
    $('#userpanel').hide();
    lookTablePrincipal();
})


//страница юзера по боковой ссылке, вставится в admin-page.html по ссылке
async function lookTablePrincipal() {
    let princ = await fetch("http://localhost:8080/api/info").then(r => r.json());
    $('#centralTable').hide();
    let rol = "";
    princ.roles.forEach((u) => {
        rol += u.withoutPrefix + "  \n";
    });
    let strPr = `<tr><td>${princ.id}</td>
                            <td>${princ.username}</td>
                            <td>${princ.email}</td>
                            <td>${princ.firstName}</td>
                            <td>${princ.lastName}</td>
                            <td>${rol}</td></tr>`;
    let tab = `
                        <div class="col-11 bg-light vh-150 p-4">
                        <h1>User information-page</h1> 
                        <div class=" show active border" id="user_panel" role="tabpanel" aria-labelledby="home-tab">
                        <td class="bg-white"></td>
                        <h4 class="px-5">About User</h4>
                        <table class="table table-striped ">
                        <thead>
                        <th>ID</th>
                        <th>Username</th>
                        <th>Email</th>
                        <th>First Name</th>
                        <th>Last Name</th>
                        <th>Roles</th>
                        </thead>         
                        <tbody id="tbodyPrincip">${strPr}</tbody>
                        </table>
                        </div>
                        </div>`
    document.getElementById('us_tab').innerHTML = tab;
}


//показать текущего user Principal
$('.action').on('click', async function () {
    lookTablePrincipal();
});
$(document).ready(async function () {
    let principal = await fetch("http://localhost:8080/api/info").then(r => r.json());
    let rol = "";
    principal.roles.forEach((r) => {
        rol += r.withoutPrefix + "  "
    });
    document.getElementById('black').innerHTML = `<b>${principal.username}</b>` + ' with roles: ' + `<b>${rol}</b>`;
})

// создание нового юзера
const usernameCreate = document.getElementById('usernameCreate');
const emailCreate = document.getElementById('emailCreate');
const firstNameCreate = document.getElementById('firstNameCreate');
const lastNameCreate = document.getElementById('lastNameCreate');
const passwordCreate = document.getElementById('passwordCreate');

$(document).ready(async function () {
    $('.btnCreate').on('click', async function (e) {
        e.preventDefault();                                                       // отмена действия браузера для своего события
        let u = await fetch('http://localhost:8080/api/create', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({                       // для преобразования обьектов в json
                username: usernameCreate.value,
                email: emailCreate.value,
                firstName: firstNameCreate.value,
                lastName: lastNameCreate.value,
                password: passwordCreate.value,
                roles: $('#select').val()
            })
        }).then(f => f.json());
        usernameCreate.value = "";
        emailCreate.value = "";
        firstNameCreate.value = "";
        lastNameCreate.value = "";
        passwordCreate.value = "";
        document.getElementById('select').value = "";
        document.getElementById('ad_pan').click();
        let rol = "";
        u.roles.forEach((u) => {
            rol += u.withoutPrefix + " \n";
        });
        row = ` <tr id="dele${u.id}"><td>${u.id}</td>
                                    <td>${u.username}</td>
                                    <td>${u.email}</td>
                                    <td>${u.firstName}</td>
                                    <td>${u.lastName}</td>
                                    <td style="display:none;">${u.password}</td>
                                    <td>${rol}</td>
                                    <td><button class="btn btn-info editbtn">Edit</button></td>
                                    <td><button class="btn btn-danger delbtn">Delete</button></td></tr>`;
        tbody.insertAdjacentHTML('beforeend', row);
    })
})

const idEdit = document.getElementById('idFormEdit');
const usernameEdit = document.getElementById('usernameFormEdit');
const emailEdit = document.getElementById('emailFormEdit');
const firstNameEdit = document.getElementById('firstNameFormEdit');
const lastNameEdit = document.getElementById('lastNameFormEdit');
const passwordEdit = document.getElementById('passwordFormEdit');

let row = "";


//кнопка Edit в модальном окне
$(document).ready(function () {
    $('#submitEdit').on('click', async function (e) {
        e.preventDefault();
        let id = idEdit.value;
        let one = document.getElementById(`dele${id}`);
        let u = await fetch('http://localhost:8080/api/update', {
            method: 'PATCH',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({
                id: idEdit.value,
                username: usernameEdit.value,
                email: emailEdit.value,
                firstName: firstNameEdit.value,
                lastName: lastNameEdit.value,
                password: passwordEdit.value,
                roles: $('#selectEdit').val()
            })
        }).then(u => u.json());
        let rol = "";
        u.roles.forEach((u) => {
            rol += u.withoutPrefix + " \n";
        });
        row = ` <tr id="dele${u.id}"><td>${u.id}</td>
                                    <td>${u.username}</td>
                                    <td>${u.email}</td>
                                    <td>${u.firstName}</td>
                                    <td>${u.lastName}</td>
                                    <td style="display:none;">${u.password}</td>
                                    <td>${rol}</td>
                                    <td><button class="btn btn-info editbtn">Edit</button></td>
                                    <td><button class="btn btn-danger delbtn">Delete</button></td></tr>`;
        one.innerHTML = row;
        $('#editModal').modal('hide');
    })
});

//поиск  юзера в таблице, соответствующего  selector и выполняющего с ним функцию handle (удаление, редактирование)
const on = (element, even, selector, handle) => {
    element.addEventListener(even, e => {//назначение более одного обработчика события
        if (e.target.closest(selector)) {// метод поднимается вверх от элемента и проверяет каждого родителя, прекращается поиск при соотвествии селектору
            handle(e)
        }
    })
}

//для модального окно при изменении
on(document, 'click', '.editbtn', (e) => {
    e.preventDefault();
    const father = e.target.parentNode.parentNode; // возвращает родителя
    const id = father.firstElementChild.innerHTML;
    const username = father.children[1].innerHTML;
    const email = father.children[2].innerHTML;
    const firstName = father.children[3].innerHTML;
    const lastName = father.children[4].innerHTML;
    const password = father.children[5].innerHTML;
    const role = father.children[6].innerHTML;
    let rolAr = role.replace(/[^A-Za-z]+/, " ").trim().split(/\s+/);
    rol(rolAr);
    $('#idFormEdit').val(id);
    $('#usernameFormEdit').val(username);
    $('#emailFormEdit').val(email);
    $('#firstNameFormEdit').val(firstName);
    $('#lastNameFormEdit').val(lastName);
    $('#passwordFormEdit').val(password);
    $('#editModal').modal();

    console.log('-----------------');
    console.log('-----------------');
    console.log(father);
    console.log(id);
    console.log(username);
    console.log(email);
    console.log(firstName);
    console.log(lastName);
    console.log(password);
    console.log(role);
    console.log(rolAr);

})

const idValue = document.querySelector('#idDelete');
//для модального окна при удалении
on(document, 'click', '.delbtn', (e) => {
    e.preventDefault();
    const father = e.target.parentNode.parentNode; // возвращает родителя
    const id = father.firstElementChild.innerHTML;
    const username = father.children[1].innerHTML;
    const email = father.children[2].innerHTML;
    const firstName = father.children[3].innerHTML;
    const lastName = father.children[4].innerHTML;
    const password = father.children[5].innerHTML;
    const role = father.children[6].innerHTML;
    $('#idDelete').val(id);
    $('#usernameDelete').val(username);
    $('#emailDelete').val(email);
    $('#firstNameDelete').val(firstName);
    $('#lastNameDelete').val(lastName);
    $('#passwordDelete').val(password);
    $('#roleDelete').val(role);
    $('#deleteModal').modal();
})

//кнопка Delete в модальном окне
$(document).ready(function () {
    $('#submitDelete').on('click', async function (e) {
        e.preventDefault();
        let id = idValue.value;
        let deleteUser = document.getElementById(`dele${id}`);
        await fetch(`http://localhost:8080/api/delete/${id}`, {
            method: 'DELETE'
        });
        deleteUser.remove();
        $('#deleteModal').modal('hide');
    });
});