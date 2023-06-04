import { SET_RESPONSE_DATA } from './actions';

const initialState = {
    responseData: null,
};

export function rootReducer(state = initialState, action) {
    switch (action.type) {
        case SET_RESPONSE_DATA:
            return { ...state, responseData: action.data };
        default:
            return state;
    }
}