let fileId = 100;
let ingredientId = document.getElementById("ingredients-count").value;

function addElement(parentId, elementTag, elementId, elementClass, html) {

    let parent = document.getElementById(parentId);
    let newElement = document.createElement(elementTag);
    newElement.setAttribute('id', elementId);
    newElement.setAttribute('class', elementClass)
    newElement.innerHTML = html;
    parent.appendChild(newElement);
}

function addFile() {

    fileId++;
    let html =
        `<div class="upload-file">
            <div><span>Image:</span></div>
            <div>
                <input type="file" accept="image/*" name="photo-file" class="photo-file" id="file-${fileId}">
            </div>
            <div class="align-sign-minus"><input class="sign-minus" type="button" value="X" onclick="removeElement('files-${fileId}')"></div>
        </div>
        <div class="upload-description">
            <span class="upload-description-lbl">Description:</span>
            <div class="upload-description-text">
                <input type="text" name="photo-description" class="photo-description">
            </div>
        </div>
        <div class="upload-primary">
            <div class="upload-primary-lbl"><span>Primary:</span></div>
            <div class="upload-primary-radio"><input type="radio" name="photo-primary" value="file-${fileId}"></div>
        </div>`

    addElement('photos', 'div', 'files-' + fileId, 'upload-photo', html);
}

function addIngredient() {

    ingredientId++;
    let html =
        `<input class="ingredient-name" type="text" id="ingredient-name-${ingredientId}" name="ingredientDTOS[${ingredientId}].ingredientName">` +
        `<input class="ingredient-quantity" type="text" id="ingredient-quantity-${ingredientId}" name="ingredientDTOS[${ingredientId}].quantity">` +
        `<input class="ingredient-unit" list="units" id="ingredient-unit-${ingredientId}" name="ingredientDTOS[${ingredientId}].unitName">` +
        `<input class="sign-minus" type="button" value="X" onclick="removeElement('ingredient-box-${ingredientId}')">`;

    addElement('ingredient-boxes', 'div', 'ingredient-box-' + ingredientId, 'ingredient-box', html);
}

function removeElement(elementId) {

    let element = document.getElementById(elementId);
    element.parentNode.removeChild(element);
}

let titleInput = document.getElementById("title-input");

checkTitle = (event) => {
    let recipeTitle = event.target.value;

    if (recipeTitle > 3) {

        let titleErrorElement = document.getElementById("title-error");

        titleErrorElement.replaceChildren();

        fetch(`/api/recipes/isAvailable/${recipeTitle}`)
            .then(response => {
                if (!response.ok) {
                    throw new Error(`HTTP error! Status: ${response.status}`);
                }
                return response.json()
            })
            .then(isAvailable => {
                if (isAvailable === false) {
                    let smallElement = document.createElement("small");
                    smallElement.setAttribute("class", "error-text")
                    smallElement.innerText = "You already have recipe with the same title.";
                    titleErrorElement.appendChild(smallElement);
                }
            })
            .catch(error => console.log(error));
    }
};

titleInput.addEventListener("change", checkTitle);


// let addFormElement = document.getElementById("add-form");
//
// function postData(event) {
//
//     event.preventDefault();
//     let formData = new FormData(addFormElement);
//     console.log(formData);
//     let entries = Object.fromEntries(formData);
//     console.log(entries);
// }
//
// let submitBtnElement = document.getElementById("submit-btn-add-recipe");
// submitBtnElement.addEventListener("click", postData);

