let likedTrueElement = document.getElementById('likedTrue');
let likedFalseElement = document.getElementById('likedFalse');
let isLikedElement = document.getElementById('isLiked');
let isLiked = isLikedElement.value === 'true';

let csrfHeaderName = document.getElementById("csrf").getAttribute("name");
let csrfHeaderToken = document.getElementById("csrf").getAttribute("value");

function showLiked() {

    likedTrueElement.style.display = (isLiked) ? 'block' : 'none';
    likedFalseElement.style.display = (isLiked) ? 'none' : 'block';
}

function toggleLiked() {

    isLiked = !isLiked;

    let recipeId = document.getElementById('recipe-id').getAttribute('value');
    let pathEnd = (isLiked) ? 'like' : 'unlike';

    fetch(`/api/recipes/${recipeId}/${pathEnd}`, {
        method: "POST",
        headers: {
            [csrfHeaderName]: csrfHeaderToken
        }
    })
        .then(function (response) {
            if (response.ok) {
                return response.json();
            } else {
                isLiked = !isLiked;
            }
        })
        .then(function (data) {
            let count = data.message;
            document.getElementById('likes-counter').innerText = count;
            isLikedElement.value = isLiked.toString();
            showLiked();
        });
}

function deleteRecipe() {
    //TODO Delete Recipe
    console.log('Implement me please!')
}

window.addEventListener("load", showLiked);
