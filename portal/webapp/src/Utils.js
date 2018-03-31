/**
 * Created by fran on 1/29/18.
 */
import env from "./env";
import moment from "moment";
import Immutable from "immutable";
import * as types from "./redux/types";

export function clone(obj) {
    return Object.assign({}, obj);
}

export function clear(form) {
    let cloneObj = Object.assign({}, form);
    for (let prop in cloneObj) {
        if (cloneObj.hasOwnProperty(prop))
            cloneObj[prop] = '';
    }
    return cloneObj;
}

export function copyProps(formSrc, formDest) {
    for (let prop in formSrc) {
        if (formDest.hasOwnProperty(prop))
            formDest[prop] = formSrc[prop];
    }
}

export function buildUrl(path) {
    return `${env.restServer}${path}`
}

export function hasText(value) {
    return !isEmpty(value)
}

export function isEmpty(value) {
    return (!value || 0 === value.length);
}

export function validDate(value) {
    return value && moment(value).isValid();
}

export function format(date, format) {
    return moment(date).format(format);
}

export function freezeObject(obj) {
    return Immutable.fromJS(obj).toJS();
}

export function normalizeEnumValue(value) {
    return ((value && value === "-1") || !value) ? null : value;
}

export function errorObj(response, dispatch){
    if(dispatch)
        dispatch({type: types.CANCEL_BLOCK_UI});
    return {title: response.titleMessage, messages: response.validationMessages};
}

export function isEmptyValue(value) {
    return !value || value === "-1" || value === null;
}
