export const SET_RESPONSE_DATA = 'SET_RESPONSE_DATA';

export function setResponseData(data) {
    return { type: SET_RESPONSE_DATA, payload: data };
}
