//VARIABLES
let fileIdx = document.getElementsByClassName("photo-box").length;
let ingredientIdx = document.getElementsByClassName("ingredient-box").length;
let csrfHeaderName = document.getElementById("csrf").getAttribute("name");
let csrfHeaderToken = document.getElementById("csrf").getAttribute("value");


//FUNCTIONS
function ensureDefaultIngredientInputs() {

    if (ingredientIdx === 0) {
        addIngredient();
    }
}

function addElement(parentId, elementTag, elementId, elementClass, html) {

    let parent = document.getElementById(parentId);
    let newElement = document.createElement(elementTag);
    newElement.setAttribute("id", elementId);
    newElement.setAttribute("class", elementClass)
    newElement.innerHTML = html;
    parent.appendChild(newElement);
}

function removeElement(elementId) {

    let element = document.getElementById(elementId);
    element.parentNode.removeChild(element);
}

function addIngredient() {

    ingredientIdx++;
    let html =
        `
        <div class="grid-ingredient angle-border">
            <input type="text" 
                   id="ingredient-name-${ingredientIdx}" class="ingredient-name left" 
                   name="listIngredientBM[${ingredientIdx}].ingredientName"
                   placeholder="Name">
            <input type="text" 
                   id="ingredient-quantity-${ingredientIdx}" class="ingredient-quantity center" 
                   name="listIngredientBM[${ingredientIdx}].quantity"
                   placeholder="Quantity">
            <input list="units" 
                   id="ingredient-unit-${ingredientIdx}" class="ingredient-unit right" 
                   name="listIngredientBM[${ingredientIdx}].unitName"
                   placeholder="Unit">
            <input type="button" 
                   class="sign-x btn-x" 
                   value="X" onclick="removeElement('ingredient-box-${ingredientIdx}')">
        </div>
        `;

    addElement("ingredient-boxes", "div", `ingredient-box-${ingredientIdx}`, "ingredient-box", html);
}

function checkTitle(event) {
    let recipeTitle = event.target.value;

    if (recipeTitle.length > 2) {

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
                    let listElement = document.createElement("ul");
                    let itemElement = document.createElement("li");
                    itemElement.innerText = "You already have recipe with the same title.";
                    listElement.appendChild(itemElement);
                    titleErrorElement.appendChild(listElement);
                }
            })
            .catch(error => console.log(error));
    }
}

function uploadPhoto() {


    let fileData = document.getElementById("new-photo-file-data").files[0];
    let description = document.getElementById("new-photo-description-text").value;
    let tempRecipeId = document.getElementById("recipe-bm-id").getAttribute("value");

    let newFileErrorCtr = document.getElementById("new-photo-error-ctr")
    // let newDescriptionErrorCtr = document.getElementById("new-photo-description-error-ctr")

    newFileErrorCtr.replaceChildren();
    // newDescriptionErrorCtr.replaceChildren()

    let formData = new FormData();
    formData.append("fileData", fileData);
    formData.append("description", description);
    formData.append("tempRecipeId", tempRecipeId);

    fetch("/api/photos/temp/upload", {
        method: "POST",
        headers: {
            [csrfHeaderName]: csrfHeaderToken
        },
        body: formData
    }).then(response => {
        return response.ok
            ? response.json().then(data => addPhoto(data))
            : response.json().then(data => showErrors(data));
    });

    function showErrors(data) {

        let containerElement = document.createElement("div");
        containerElement.classList.add("grid-label-input-ctr")
        containerElement.classList.add("lbl80")

        let listElement = document.createElement("ul");
        listElement.classList.add("grid-input");
        listElement.classList.add("errors-ctr");
        containerElement.appendChild(listElement);

        if (data.errors.fileData) {

            for (err of data.errors.fileData) {
                let element = document.createElement("li");
                element.innerText = err;
                listElement.appendChild(element)
            }
        }

        if (data.errors.description) {

            for (err of data.errors.description) {
                let element = document.createElement("li");
                element.innerText = err;
                listElement.appendChild(element)
            }
        }

        if (data.errors.fileData || data.errors.description) {

            newFileErrorCtr.appendChild(containerElement);
        }
    }
}

function addPhoto(photo) {

    resetPhotoInput();

    let html =
        `
        <div id="photo-box-${fileIdx}" class="grid-photo">
            <input type="hidden" id="photo-id-${fileIdx}" name="recipeBM.photoVMList[${fileIdx}].tempId}" value="${photo.tempId}">
            <input type="hidden" name="recipeBM.photoVMList[${fileIdx}].tempRecipeId}" value="${photo.tempRecipeId}">
            
            <label class="grid-photo-left">
                <input id="photo-primary-${photo.tempId}" class="grid-photo-left photo-primary" 
                       name="primaryPhotoId" value="${photo.tempId}" 
                       type="radio">
            </label>
        
            <div class="grid-photo-center">
                <img class="photo-file" src=${photo.url} alt="${photo.description}">
            </div>
            
            <div class="grid-photo-right align-sign-minus">
                <input type="button" 
                       class="sign-x" 
                       value="X" onclick="removePhoto('${fileIdx}')">
            </div>
            
            <div class="grid-photo-bottom photo-filename">${photo.filename}</div>    
        </div>
        `;


    addElement("photos-ctr",
        "div",
        `photo-box-${fileIdx++}`,
        "photo-box",
        html)
    ;
}

function resetPhotoInput() {

    let parent = document.getElementById("new-photo-ctr");

    parent.innerHTML =
        `
        <div class="grid-label-input-ctr lbl80 new-photo-file">
            <label class="grid-label">File:</label>
            <div class="angle-border grid-input">
                <input id="new-photo-file-data" class="new-photo-file-data"
                       type="file" accept="image/*">
            </div>
        </div>

        <div class="grid-label-input-ctr lbl80">
            <label for="new-photo-description-text"
                   id="new-photo-description-lbl" class="grid-label">Description:</label>
            <div class="grid-input angle-border">
                <input id="new-photo-description-text"
                       type="text" placeholder="Photo Description">
            </div>
        </div>
        
        <div id="new-photo-error-ctr"></div>

        <div class="grid-label-input-ctr lbl80">
            <input id="upload-btn-new-photo" class="grid-input submit-btn" type="button" value="Upload"
                   onclick="uploadPhoto();">
        </div>
    `;

}

function removePhoto(idx) {

    let errorsElement = document.getElementById("photos-error-ctr");
    errorsElement.replaceChildren();

    let parentElement = document.getElementById("photos-ctr");
    let childElement = document.getElementById(`photo-box-${idx}`);
    let id = document.getElementById(`photo-id-${idx}`).value;

    if (!childElement) {
        return;
    }

    fetch(`/api/photos/temp/delete/${id}`, {
        method: "DELETE",
        headers: {
            [csrfHeaderName]: csrfHeaderToken
        }
    }).then(response => process(response));

    function process(response) {
        if (response.ok) {
            parentElement.removeChild(childElement);
        } else {
            response.json().then(data => {
                let listItem = document.createElement("li");
                listItem.innerText = data.message;
                let unorderedList = document.createElement("ul");
                unorderedList.appendChild(listItem);
                errorsElement.appendChild(unorderedList);
            });
        }
    }

}

function submitRenumbered(event) {

    event.preventDefault();

    renumberPhotoBoxes();
    renumberIngredientBoxes();

    document.getElementById("recipe-add-form").submit();
}

function renumberPhotoBoxes() {
    let photoBoxes = document.getElementsByClassName("photo-box");
    let photoFiles = document.getElementsByClassName("photo-file");
    let photoDescriptions = document.getElementsByClassName("photo-filename");

    for (let idx = 0; idx < photoBoxes.length; idx++) {
        photoFiles[idx].name = `photoUploadDTOS[${idx}].file`
        photoDescriptions[idx].name = `photoUploadDTOS[${idx}].description`
    }
}

function renumberIngredientBoxes() {
    let ingredientBoxes = document.getElementsByClassName("ingredient-box");
    let ingredientNames = document.getElementsByClassName("ingredient-name");
    let ingredientQuantities = document.getElementsByClassName("ingredient-quantity");
    let ingredientUnits = document.getElementsByClassName("ingredient-unit");

    for (let idx = 0; idx < ingredientBoxes.length; idx++) {
        ingredientNames[idx].name = `listIngredientBM[${idx}].ingredientName`;
        ingredientQuantities[idx].name = `listIngredientBM[${idx}].quantity`;
        ingredientUnits[idx].name = `listIngredientBM[${idx}].unitName`;
    }
}


//EVENT LISTENERS
window.addEventListener("load", ensureDefaultIngredientInputs);

document.getElementById("title-input").addEventListener("change", checkTitle);

document.getElementById("submit-btn-add-recipe").addEventListener("click", submitRenumbered);
