from selenium import webdriver
from bs4 import BeautifulSoup
import warnings
import requests
import json


def get_started():
    global driver, soup, url
    driver = webdriver.Chrome()
    url = "https://www.expat.com/en/business/africa/tunisia/"
    driver.get(url)
    driver.maximize_window()
    print("selection de bouton categories ..")
    category_button = driver.find_element_by_xpath(
        '//*[@id="select2-select-profession-container"]')
    category_button.click()

    page = driver.execute_script(
        "return document.getElementsByTagName('html')[0].innerHTML")
    soup = BeautifulSoup(page, 'lxml')


def new_soup():
    page = driver.execute_script(
        "return document.getElementsByTagName('html')[0].innerHTML")
    new = BeautifulSoup(page, 'lxml')
    return new


def get_categories():

    list_categories = []
    categories_tag = list(soup.find_all("span", class_="category"))
    for element in categories_tag:
        list_categories.append(str(element).split(
            ">")[1].split("<")[0].replace("&amp;", "and"))
    return list_categories


def get_links(categories):
    links = []
    id_tags = soup.find_all("li", class_="select2-results__option")
    id_list = list(id_tags)
    for element in id_list:
        if str(element).find("catID#") != -1:  # get only the main titles in the list
            id = str(element).split("#")[1].split("\"")[0]
            links.append(id)

    # 5 - join ids and names to get the link :
    for i in range(len(links)):
        links[i] += "_" + \
            '-'.join(categories[i].lower().replace("&amp;", "").split())

    return links


def find_all_urls_of_category(soup):
    tags = list(soup.find_all("li", class_="pagination-button"))
    liste = []
    for l in range(len(tags)):
        if l == 0:
            continue
        else:
            liste.append(str(tags[l]).split("href=\"")[1].split("\">")[0])
    return liste


def get_info_by_class(soup, class_value):
    # cast : find_all method returns a resultset
    tags = list(soup.find_all("span", class_=class_value))
    liste = []
    for tag in tags:
        liste.append(str(tag).split(">")[1].split("<")[0].replace("&amp;", ""))

    return liste


def get_info_by_itemprop(soup, itemprop_value):
    tags = list(soup.find_all("span", itemprop=itemprop_value))
    liste = []
    for tag in tags:
        liste.append(str(tag).split(">")[1].split("<")[0].replace("\n", ""))

    return liste


def add_data(key, list1, list2, list3):
    # gather data in one list where list item : [name,phone,address]
    for i, j, k in zip(list1, list2, list3):
        business = []
        business.append(i)
        business.append(j)
        business.append(k)
        data[key].append(business)


def get_data_from_current_page(soup, key):
    # 1 - names :
    names = get_info_by_class(soup, "business-name")
    # 2 - phone numbers :
    phone_numbers = get_info_by_class(soup, "phone-number")
    # 3 - addresses :
    # the address is divided into 2 tags
    adresses = []
    addresses_part1 = get_info_by_itemprop(soup, "address")
    addresses_part2 = get_info_by_itemprop(soup, "addressRegion")
    for i, j in zip(addresses_part1, addresses_part2):
        adresses.append(i+j)

    # insert into data base:

    for name, number, adr in zip(names, phone_numbers, adresses):
        print(name, number, adr)
        url1 = 'http://localhost:8080/businesses'
        myobj = {'address': str(adr), 'category': str(key),
                 'name': str(name), 'phoneNumber': str(number)}
        jsonObj = json.dumps(myobj)
        try:
            x = requests.post(url=url1, data=jsonObj)
        except requests.ConnectionError as err:
            print('*****************************')
            print(str(err))
            print('*****************************')


if __name__ == '__main__':
    # initiate the main list which will contain all scraped data
    global data
    data = {}
    # data is a dict where keys are the categories and values are lists of business info
    print("STARTING  ...")
    warnings.filterwarnings("ignore")
    get_started()

    print("GETTING CATEGORIES ...")
    categories = get_categories()

    print("Getting all links of pages of every categorie ..")
    links = get_links(categories)
    # iterate the list of links -> get the data :
    print("START SCRAPPING DATA ..")
    for k in range(len(links)):
        key = categories[k]
        print("**********************************************************************************")
        indice = k+1
        print("CATEGORIE {} : {}".format(indice, key))
        print("page 1")
        data[key] = []

        new_url = "https://www.expat.com/en/business/africa/tunisia/" + \
            links[k]+"/"
        driver.get(new_url)
        current_soup = new_soup()
        get_data_from_current_page(current_soup, key)
        # data from remaining pages of the same categories
        urls = find_all_urls_of_category(current_soup)
        if urls != []:
            i = 2
            for url in urls:
                print("page {}".format(i))
                i = i+1
                driver.get(url)
                current_soup = new_soup()
                get_data_from_current_page(current_soup, key)
        

    driver.close()
