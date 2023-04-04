function renderUserData(id) {

    fetch(`/api/users/${id}`)
        .then(res => res.json())
        .then((result) => renderGreetingHtml(result))
        .catch((error) => {
            console.error("Error:", error);
        });

}

function renderGreetingHtml(result) {
    let content = document.getElementById('content');
    content.innerHTML = `<p>Hello, ${result.displayName}</p>`;
}



let index = document.URL.lastIndexOf("/");
let userId = document.URL.substring(index + 1);
renderUserData(userId);