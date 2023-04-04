let csrfHeaderName = document.getElementById("csrf").getAttribute("name");
let csrfHeaderToken = document.getElementById("csrf").getAttribute("value");

function toggleActive(userId) {

    let checkbox = document.getElementById(`active-${userId}`);
    let isChecked = checkbox.checked;
    //reset until fetch returns success
    checkbox.checked = !isChecked;

    let action = isChecked ? 'activate' : 'inactivate';

    fetch(`/api/admin/users/${userId}/${action}`, {
        method: "POST",
        headers: {
            [csrfHeaderName]: csrfHeaderToken
        }
    })
        .then(function (response) {
            if (response.ok) {
                checkbox.checked = isChecked;
            } else {
                return response.json().then(error => {
                    let message = error.message;
                    window.alert(message);
                });
            }
        });
}

function toggleModerator(userId) {

    let checkbox = document.getElementById(`moderator-${userId}`);
    let isChecked = checkbox.checked;
    //reset until fetch returns success
    checkbox.checked = !isChecked;

    let action = isChecked ? 'promote' : 'demote';

    fetch(`/api/admin/moderators/${userId}/${action}`, {
        method: "POST",
        headers: {
            [csrfHeaderName]: csrfHeaderToken
        }
    })
        .then(function (response) {
            if (response.ok) {
                checkbox.checked = isChecked;
            } else {
                return response.json().then(error => {
                    let message = error.message;
                    window.alert(message);
                });
            }
        });
}