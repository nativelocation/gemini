import * as constants from '../constants'

export const loginUser = (data) => (dispatch) => {
    dispatch({
        type: constants.LOGIN_START,
        data: data
    })
}
