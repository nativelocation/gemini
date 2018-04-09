import * as constants from '../constants'

const initialState = {
    form: {
        email: 'default@gmail.com',
        password: 'default'
    }           
}

export default function loginStore(state = initialState, action) {
    switch (action.type) {
        case constants.LOGIN_START:
            return state
        default:
            return state
    }
}