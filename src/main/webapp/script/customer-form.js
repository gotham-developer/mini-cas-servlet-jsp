/*  ----------------------------------------
    Populate Dropdowns with Postman APIs
----------------------------------------  */
const API_COUNTRIES = 'https://countriesnow.space/api/v0.1/countries/iso';
const API_COUNTRIES_CODES = 'https://countriesnow.space/api/v0.1/countries/codes';
const API_STATES = 'https://countriesnow.space/api/v0.1/countries/states';
const API_CITIES = 'https://countriesnow.space/api/v0.1/countries/state/cities';

let countriesList = [];
let countriesCodesList = [];
let statesList = {};

// ---------- Populate countries & nationality and store in array ----------
$.getJSON(API_COUNTRIES, function (res) {
  if (res.error) return;

console.log("getting countries...")
  countriesList = res.data;
  let nationalityOptions = '<option value="">Select</option>';
  let countryOptions = '<option value="">Select</option>';

  countriesList.forEach((c) => {
    // Default selection only for nationality, if desired
    let selected = c.name === 'India' ? 'selected' : '';
    nationalityOptions += `<option value="${c.name}" ${selected}>${c.name}</option>`;
    countryOptions += `<option value="${c.name}">${c.name}</option>`;
  });

  $('#nationality').html(nationalityOptions);
  $('#country').html(countryOptions);
});

// ---------- Populate Country Codes and store in array ----------
$.getJSON(API_COUNTRIES_CODES, function (res) {
  if (res.error) return;
  console.log("populating country codes...")
  countriesCodesList = res.data;
  let countryCodeOptions = '<option value="">Select</option>';
  countriesCodesList.forEach((c) => {
    let selected = c.name === 'India' ? 'selected' : '';
    countryCodeOptions += `<option value="${c.code}" data-dial="${c.dial_code}" ${selected}>${c.code}</option>`;
  });
  $('#countryCode').html(countryCodeOptions);
});

// ---------- Store states in list ----------
$.getJSON(API_STATES, function (res) {
  if (res.error) return;
  res.data.forEach((c) => {
    statesList[c.name] = c.states.map((s) => s.name);
  });
});

// ---------- Populate States ----------
function populateStates(countryName) {
console.log("populating states...")
  let options = '<option value="">Select</option>';
  if (statesList[countryName]) {
    statesList[countryName].forEach((s) => (options += `<option value="${s}">${s}</option>`));
  }
  $('#state').html(options);
  $('#city').html('<option value="">Select</option>');
  $('#nationality').val(countryName);
}

// ---------- Populate Cities ----------
function populateCities(countryName, stateName) {
  if (!countryName || !stateName) return;

  $.post(API_CITIES, { country: countryName, state: stateName }, function (res) {
    let options = '<option value="">Select</option>';
    if (res.error === false && res.data && res.data.length > 0) {
      res.data.forEach((c) => (options += `<option value="${c}">${c}</option>`));
    }
    $('#city').html(options);
  });
}

// ---------- Country Change ----------
$('#country').on('change', function () {
  const selectedCountry = $(this).val();
  populateStates(selectedCountry);
});

// ---------- State Change ----------
$('#state').on('change', function () {
  const stateName = $(this).val();
  const countryName = $('#country').val();
  populateCities(countryName, stateName);
});

// When user changes the country select
$('#countryCode').change(function () {
  let dial = $(this).find(':selected').data('dial');
  $('#dialCode').val(dial);
});

// if user manually changes dialCode, select the country automatically
$('#dialCode').on('input', function () {
  let val = $(this).val().toUpperCase();
  $('#countryCode option').each(function () {
    if ($(this).data('dial') === val) {
      $(this).prop('selected', true);
    }
  });
});

/*  -----------------------
    Section Progression
-----------------------  */
// Initially hide all sections except first
$('#addressInformationSection, #educationInformationSection').hide();

// Proceed button for Personal Information
$('#personal-detail-pcd-btn').on('click', function () {
  if (validateSection('#personalInformationSection')) {
    $('#addressInformationSection').slideDown();
  }
});

// Proceed button for Address Information
$('#address-detail-pcd-btn').on('click', function () {
  if (validateSection('#addressInformationSection')) {
    $('#educationInformationSection').slideDown();
  }
});

// Toggle functionality on section headers
$('.card-header').on('click', function () {
  $(this).next('.card-body').slideToggle();
  $(this).find('.dropdown-icon').toggleClass('bi-chevron-right bi-chevron-down');
});

// Check validity of section
function validateSection(sectionId) {
  let isValid = true;
  $(sectionId + ' .form-control[required], ' + sectionId + ' .form-select[required]').each(function () {
    if ($(this).val() === '') {
      $(this).addClass('is-invalid');
      isValid = false;
    } else {
      $(this).removeClass('is-invalid');
    }
  });
  return isValid;
}

// Disable proceed buttons initially
$('#personal-detail-pcd-btn, #address-detail-pcd-btn').prop('disabled', true);

// Bootstrap-style validation watcher
$('form input, form select').on('input change', function () {
  const section = $(this).closest('section');
  const invalid = section.find(':invalid').length > 0;
  section.find('button.btn-primary').prop('disabled', invalid);
});

// Add Row functionality
let tableBody = $('#educationInformationSection table tbody');
$('#addRowBtn').click(function () {
  let lastRow = tableBody.find('tr:last');
  let newRow = lastRow.clone(true); // clone including events

  const newIndex = tableBody.find('tr').length;
  newRow.attr('data-row-index', newIndex);

  // Clear input/select values in new row
  newRow.find('input', 'select').not('.highest-degree-radio').val('');

  const radio = newRow.find('.highest-degree-radio');
  radio.prop('checked', false);
  radio.val(newIndex);

  tableBody.append(newRow);
});

// Delete Row functionality
$('#educationInformationSection').on('click', 'i', function () {
  let tableBody = $('#educationInformationSection table tbody');
  if (tableBody.find('tr').length > 1) {
    $(this).closest('tr').remove();
  } else {
    toastr.options = {
      closeButton: true,
      progressBar: true,
      positionClass: 'toast-bottom-right',
      timeOut: '3000',
    };
    toastr.warning('At least one row must remain');
  }
});

/*  --------------------------
    Save & Next validation
--------------------------  */
const $customerForm = $('#customerForm');
const $saveNextBtn = $('#saveNextBtn');

function toggleSaveNextButton() {
  const form = $customerForm.get(0);
  if (!form) return;

  if (form.checkValidity()) {
    $saveNextBtn.prop('disabled', false);
  } else {
    $saveNextBtn.prop('disabled', true);
  }
}

// Initial check
toggleSaveNextButton();

// Whenever any input/select changes, re-check
$customerForm.on('input change', 'input, select, textarea', toggleSaveNextButton);
