const loanData = {
  productTypes: [
    {
      id: 'homeLoan',
      name: 'Home Loan',
      loanProducts: [
        {
          id: 'standardHomeLoan',
          name: 'Standard Home Loan',
          schemes: ['Fixed Interest Scheme', 'Floating Interest Scheme', 'Tax Benefit Scheme'],
        },
        {
          id: 'premiumHomeLoan',
          name: 'Premium Home Loan',
          schemes: ['Reduced EMI Scheme', 'Top-up Loan Scheme'],
        },
      ],
    },
    {
      id: 'carLoan',
      name: 'Car Loan',
      loanProducts: [
        {
          id: 'newCarLoan',
          name: 'New Car Loan',
          schemes: ['Zero Processing Fee', 'Low Interest Rate'],
        },
        {
          id: 'usedCarLoan',
          name: 'Used Car Loan',
          schemes: ['Flexible Tenure Scheme', 'Quick Approval Scheme'],
        },
      ],
    },
    {
      id: 'personalLoan',
      name: 'Personal Loan',
      loanProducts: [
        {
          id: 'salaryBasedLoan',
          name: 'Salary Based Loan',
          schemes: ['Instant Disbursal Scheme', 'No Collateral Scheme'],
        },
        {
          id: 'selfEmployedLoan',
          name: 'Self Employed Loan',
          schemes: ['Higher Loan Amount Scheme', 'Longer Tenure Scheme'],
        },
      ],
    },
    {
      id: 'educationLoan',
      name: 'Education Loan',
      loanProducts: [
        {
          id: 'undergraduateLoan',
          name: 'Undergraduate Education Loan',
          schemes: ['Interest Subsidy Scheme', 'Deferred Repayment Scheme'],
        },
        {
          id: 'postgraduateLoan',
          name: 'Postgraduate Education Loan',
          schemes: ['Flexible Tenure Scheme', 'Loan Moratorium Scheme'],
        },
      ],
    },
    {
      id: 'businessLoan',
      name: 'Business Loan',
      loanProducts: [
        {
          id: 'startupLoan',
          name: 'Startup Loan',
          schemes: ['Seed Funding Scheme', 'Collateral-Free Scheme'],
        },
        {
          id: 'expansionLoan',
          name: 'Business Expansion Loan',
          schemes: ['Low Interest Scheme', 'EMI Holiday Scheme'],
        },
      ],
    },
  ],
};

const $form = $('#loan-application-form');
const $requiredFields = $form.find('[required]');
const $submitYes = $('#form-submit-yes');
const $submitNo = $('#form-submit-no');
const positiveFieldSet = new Set(['amountRequested', 'tenure', 'rate']);

// populate product type -> loan product -> scheme progressively
loanData.productTypes.forEach((pt) => {
  $('#product-type').append(`<option value="${pt.name}">${pt.name}</option>`);
});

$('#product-type').on('change', function () {
  const selectedPT = $(this).val();
  const pt = loanData.productTypes.find((p) => p.name === selectedPT);

  $('#product').empty().append('<option value="" selected hidden>Select Loan Product</option>');
  $('#scheme').empty().append('<option value="" selected hidden>Select Scheme</option>');

  if (pt) {
    pt.loanProducts.forEach((lp) => {
      $('#product').append(`<option value="${lp.name}">${lp.name}</option>`);
    });
  }

  $('#product')
    .off('change')
    .on('change', function () {
      const selectedLP = $(this).val();

      $('#scheme').empty().append('<option value="" selected hidden>Select Scheme</option>');

      const lp = pt.loanProducts.find((l) => l.name === selectedLP);
      if (lp) {
        lp.schemes.forEach((scheme) => {
          $('#scheme').append(`<option value="${scheme}">${scheme}</option>`);
        });
      }
    });
});

// Real-time validation and feedback
$requiredFields.on('input change', function () {
  const $el = $(this);
  const valid = validateField($el);

  $el.toggleClass('is-valid', valid).toggleClass('is-invalid', !valid);

  toggleSubmit();
});

// 3. Monitor Yes/No radio
$submitYes.on('change', toggleSubmit);
$submitNo.on('change', toggleSubmit);

// Validate single field (returns true/false)
function validateField($el) {
  const val = $el.val().trim();

  if ($el.is('select')) return val !== '';

  if (positiveFieldSet.has($el.attr('id'))) {
    return val !== '' && !isNaN(val) && Number(val) > 0;
  }

  if ($el.is('input[type="text"], input[type="number"]')) {
    return val !== '';
  }

  return true;
}

// Toggle the submit button when all fields are validated
function toggleSubmit() {
  let allValid = true;

  $requiredFields.each(function () {
    if (!validateField($(this))) allValid = false;
  });

  // Example: enable submit only if "Yes" is selected
  if (!$('#form-submit-yes').is(':checked')) allValid = false;

  $('#loan-application-form button[type="submit"]').prop('disabled', !allValid);
}

// run the toggle submit function once when page is opened
toggleSubmit();

// initially disable only this submit
$('#loan-application-form button[type="submit"]').prop('disabled', true);

// On form submit, final validation check
$form.on('submit', function (e) {
  let valid = true;

  $requiredFields.each(function () {
    if (!validateField($(this))) valid = false;
  });

  if (!valid) {
    e.preventDefault();
    alert('Please fill all required fields correctly.');
  }
});

// generate repay schedule
$('#generate-button').on('click', function () {
  let principal = parseFloat($('#amount-requested').val());
  let rate = parseFloat($('#rate').val());
  let tenure = parseInt($('#tenure').val());
  let tenureUnit = $('#tenure-in').val();

  if (!principal || principal <= 0 || !rate || !tenure) {
    alert('Please enter valid values for Amount, Rate and Tenure!');
    return;
  }

  let months = tenureUnit === 'Years' ? tenure * 12 : tenure;
  let monthlyRate = rate / 12 / 100;
  let emi = (principal * monthlyRate * Math.pow(1 + monthlyRate, months)) / (Math.pow(1 + monthlyRate, months) - 1);

  let tableBody = '';
  let openingBalance = principal;

  for (let i = 1; i <= months; i++) {
    let interest = parseFloat(openingBalance * monthlyRate);
    let principalComp = parseFloat(emi - interest);
    let closingBalance = parseFloat(openingBalance - principalComp);
    if (closingBalance < 0) closingBalance = 0;

    tableBody += `<tr>
                <td>${i}</td>
                <td>${formatNumber(openingBalance)}</td>
                <td>${formatNumber(emi)}</td>
                <td>${formatNumber(interest)}</td>
                <td>${formatNumber(principalComp)}</td>
                <td>${formatNumber(closingBalance)}</td>
            </tr>`;

    openingBalance = closingBalance;
  }
  $('#repayment-table tbody').html(tableBody);

  let repaymentModal = new bootstrap.Modal(document.getElementById('repayment-modal'));
  repaymentModal.show();
});

// format number to 2 decimals with country-specific formating of commas
function formatNumber(n) {
  return n.toLocaleString('en-IN', {
    minimumFractionDigits: 2,
    maximumFractionDigits: 2,
  });
}
