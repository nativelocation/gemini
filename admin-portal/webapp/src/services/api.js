// localhost:3000/srs-admin-api/auth -> POST use basic http authentication -> Basic ${btoa(credentials.username + ':' + credentials.password)}
// localhost:3000/srs-admin-api/dashboard/retrieve/regions - GET
// localhost:3000/srs-admin-api/dashboard/retrieve/cities/region/${regionIdParam} - GET
// localhost:3000/srs-admin-api/dashboard/retrieve/schools/region/${regionIdParam}/city/${cityCodeParam} - GET
// localhost:3000/srs-admin-api/dashboard/retrieve/summary - POST request body json object {regionId: null, cityCode: null, schoolId: null};

import fetch from "safe-fetch";
// import {buildUrl} from "../../Utils";

import Cookies from 'cookies-js';
// import {triggerErrorOn, triggerSessionExpiredOn} from "../actions";

fetch.cookieName = 'XSRF-TOKEN';
fetch.headerName = 'X-XSRF-TOKEN';

const DEFAULT_HEADERS = {
    Accept: 'application/json',
    "Content-Type": 'application/json'
};

const API_ROOT = 'http://localhost:3000/srs-admin-api/';


function buildUrl(path) {
    return `${API_ROOT}${path}`
}

export default class Services {

    constructor(store) {
        this.store = store;
    }


    //authentication
    token() {
        return this._getRaw("/token")
    }

    async authenticate(credentials) {
        await this.token();
        return this._login("/auth", credentials);
    }


    session() {
        return this._getPromise("/auth");
    }

    logout() {
        return this._securedPost("/logout", {});
    }


    retrieveRegions(){};

    retrieveCities(){};

    retrieveSchools(){};

    retrieveSummary(){};


    _login(path, credentials) {
        let authorization = `Basic ${btoa(credentials.username + ':' + credentials.password)}`;
        return fetch(buildUrl(path), {method: "POST", ...this._addHeader(authorization), credentials: "same-origin"})
            .then((response) => this._handleHttpCode(response, false))

    }

    _getPromise(path) {
        return fetch(buildUrl(path), this._addHeader())
            .then((response) => this._handleHttpCode(response));
    }

    _get(path) {
        return fetch(buildUrl(path), this._addHeader())
            .then((response) => this._handleHttpCode(response))
            .then((response) => response.json())
            .catch((e) => {
            })
    }

    _getRaw(path) {
        return fetch(buildUrl(path), this._addHeader())
            .then((response) => this._handleHttpCode(response))
            .then((response) => response.text())
            .catch((e) => {
            })
    }

    _publicPost(path, body, token) {
        return fetch(buildUrl(path), {
            method: "POST",
            body: JSON.stringify(body), ...this._addHeaderOnPublicPOST(token)
        })
            .then((response) => this._handleHttpCode(response, false))
            .catch((e) => {
            });
    }


    //todo: POST Methods can be improve
    _securedPost(path, body) {
        return fetch(buildUrl(path), {
            method: "POST",
            ...this._addHeader(),
            credentials: "same-origin",
            body: JSON.stringify(body)
        })
            .then((response) => this._handleHttpCode(response))
            .catch((e) => {
            });
    }

    _post(path, body) {
        return fetch(buildUrl(path), {method: "POST", body: JSON.stringify(body), ...this._addHeader()})
            .then((response) => this._handleHttpCode(response))
            .catch((e) => {
            });
    }

    _put(path, body) {
        return fetch(buildUrl(path), {method: "PUT", body: JSON.stringify(body), ...this._addHeader()})
            .then((response) => this._handleHttpCode(response))
            .catch((e) => {
            });
    }

    _addHeaderOnPublicPOST(token) {
        let headersObj = {headers: {...DEFAULT_HEADERS}};
        headersObj.headers["recaptcha-token"] = token;
        return headersObj;
    }

    _addHeader(authorization) {
        let headersObj = {headers: {...DEFAULT_HEADERS}};
        if (authorization)
            headersObj.headers.Authorization = authorization;
        return headersObj;
    }

    _handleHttpCode(response, manageException = true) {

        let httpStatus = response.status;
        if ((httpStatus >= 200 && httpStatus < 300) || (httpStatus >= 400 && httpStatus <= 402)) {
            return response;
        } else if (httpStatus === 403) {
            if (manageException) {
                // this.store.dispatch(triggerSessionExpiredOn("Su sesión ha expirado"));
            }
        } else {
            if (manageException)
                // this.store.dispatch(triggerErrorOn("Occurió un error interno, disculpe el inconveniente"));
            console.log(`internal server error, error info: ${response && response.statusText}`);
            let message = (response && response.statusText) || "unknown error";

            if (manageException) {
                console.log(message);
            }else{
                return response;
            }

        }
    }




}

