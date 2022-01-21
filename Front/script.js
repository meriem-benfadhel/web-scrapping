var businesses = [];
var categories = [];
var counts = [];

function changeFunc() {
  const selectBox = document.getElementById("select-box1");
  const selectedValue = selectBox.options[selectBox.selectedIndex].value;
  document.getElementById("label").innerHTML = selectedValue;
  affichBusiness(selectedValue);
}

async function getAllBusinesses() {
  let url = "http://localhost:8080/businesses?page=";

  for (let i = 0; i < 58; i++) {
    //
    try {
      let res = await fetch(url + i, {
        method: "GET",
        headers: { "Access-Control-Allow-Origin": "*" },
      });
      if (res.ok) {
        let data = (await res.json())["_embedded"].businesses;
        businesses = [...businesses, ...data];
      } else {
        throw Error(res.statusText);
      }
    } catch (error) {
      console.log(error);
    }
  }
}
async function InsertCategories() {
  await getAllBusinesses();
  categories = [
    ...new Set(
      businesses.map((business) => {
        return business.category;
      })
    ),
  ];

  counts = businesses.reduce((total, value) => {
    total[value.category] = (total[value.category] || 0) + 1;
    return total;
  }, {});

  let html = "";
  categories.forEach((cat) => {
    let htmlSegment = `<option value="${cat}"> ${cat}</option>`;
    html += htmlSegment;
  });
  let container = document.querySelector(".select");
  container.innerHTML = html;
}
async function affichBusiness(value) {
  let html = "";
  businesses.forEach((b) => {
    if (b.category == value) {
      let htmlSegment = `<li value="${b.name}"> <span style="font-weight: bold;">Nom business: </span>${b.name} <span style="font-weight: bold;">situé à </span> ${b.address}</li>`;
      html += htmlSegment;
    }
  });
  let container = document.querySelector(".gradient-list");
  container.innerHTML = html;
}
async function InsertBusinessOfCategory() {}

function initChart() {
  const ctx = document.getElementById("myChart").getContext("2d");
  const myChart = new Chart(ctx, {
    type: "bar",
    data: {
      labels: Object.keys(counts),
      datasets: [
        {
          label: "Number of Jobs By category",
          data: Object.values(counts),
          backgroundColor: [
            "rgba(255, 99, 132, 0.2)",
            "rgba(54, 162, 235, 0.2)",
            "rgba(255, 206, 86, 0.2)",
            "rgba(75, 192, 192, 0.2)",
            "rgba(153, 102, 255, 0.2)",
            "rgba(255, 159, 64, 0.2)",
          ],
          borderColor: [
            "rgba(255, 99, 132, 1)",
            "rgba(54, 162, 235, 1)",
            "rgba(255, 206, 86, 1)",
            "rgba(75, 192, 192, 1)",
            "rgba(153, 102, 255, 1)",
            "rgba(255, 159, 64, 1)",
          ],
          borderWidth: 1,
        },
      ],
    },
    options: {
      scales: {
        y: {
          beginAtZero: true,
        },
      },
    },
  });
}
// getCat();
(async () => {
  await InsertCategories();
  //
  initChart();
})();
