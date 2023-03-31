let likedTrueElement = document.getElementById('likedTrue');
let likedFalseElement = document.getElementById('likedFalse');
let isLikedElement = document.getElementById('isLiked');

function showLiked() {

    if (isLikedElement.value === 'true') {

        likedTrueElement.style.display = 'block';
        likedFalseElement.style.display = 'none';
    } else {

        likedTrueElement.style.display = 'none';
        likedFalseElement.style.display = 'block';
    }
}

function toggleLiked() {

    if (isLikedElement.value === 'true') {

        isLikedElement.value = 'false';
    } else {

        isLikedElement.value = 'true';
    }
    //TODO
    console.log('Send some request to database')
    showLiked();
}

function deleteRecipe() {
    //TODO
    console.log('Implement me please!')
}

window.addEventListener("load", showLiked);
