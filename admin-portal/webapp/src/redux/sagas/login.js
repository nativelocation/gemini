import { delay } from 'redux-saga'
import { put, call, takeEvery, takeLatest } from 'redux-saga/effects'

import * as constants from '../constants'

function* asyncLoginStart() {
    console.log('asyncLoginStart')
}

export function* storeLogin() {
    yield takeLatest(constants.LOGIN_START, asyncLoginStart)
}

export default [
    storeLogin()
]
