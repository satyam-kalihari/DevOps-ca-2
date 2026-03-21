const form = document.getElementById("feedbackForm");
const successMessage = document.getElementById("successMessage");

const errorElements = {
  name: document.getElementById("nameError"),
  email: document.getElementById("emailError"),
  mobile: document.getElementById("mobileError"),
  department: document.getElementById("departmentError"),
  gender: document.getElementById("genderError"),
  comments: document.getElementById("commentsError"),
};

function clearErrors() {
  Object.values(errorElements).forEach((el) => {
    el.textContent = "";
  });
}

function countWords(text) {
  return text.trim().split(/\s+/).filter(Boolean).length;
}

function validateForm() {
  clearErrors();
  successMessage.textContent = "";

  const studentName = document.getElementById("studentName").value.trim();
  const email = document.getElementById("email").value.trim();
  const mobile = document.getElementById("mobile").value.trim();
  const department = document.getElementById("department").value;
  const comments = document.getElementById("comments").value.trim();
  const selectedGender = document.querySelector('input[name="gender"]:checked');

  let isValid = true;

  if (studentName.length === 0) {
    errorElements.name.textContent = "Student Name is required.";
    isValid = false;
  }

  const emailPattern = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
  if (!emailPattern.test(email)) {
    errorElements.email.textContent = "Enter a valid email address.";
    isValid = false;
  }

  const mobilePattern = /^\d{10}$/;
  if (!mobilePattern.test(mobile)) {
    errorElements.mobile.textContent =
      "Mobile number must contain exactly 10 digits.";
    isValid = false;
  }

  if (!department) {
    errorElements.department.textContent = "Please select a department.";
    isValid = false;
  }

  if (!selectedGender) {
    errorElements.gender.textContent = "Please select a gender.";
    isValid = false;
  }

  const words = countWords(comments);
  if (!comments) {
    errorElements.comments.textContent = "Feedback comments cannot be blank.";
    isValid = false;
  } else if (words < 10) {
    errorElements.comments.textContent =
      "Feedback comments must contain at least 10 words.";
    isValid = false;
  }

  return isValid;
}

form.addEventListener("submit", (event) => {
  event.preventDefault();

  if (validateForm()) {
    form.reset();
    clearErrors();
    successMessage.textContent = "Feedback submitted successfully.";
  }
});

form.addEventListener("reset", () => {
  clearErrors();
  successMessage.textContent = "";
});
