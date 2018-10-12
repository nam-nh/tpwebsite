import axios from 'axios';
import { ICrudSearchAction, ICrudGetAction, ICrudGetAllAction, ICrudPutAction, ICrudDeleteAction } from 'react-jhipster';

import { cleanEntity } from 'app/shared/util/entity-utils';
import { REQUEST, SUCCESS, FAILURE } from 'app/shared/reducers/action-type.util';

import { INews, defaultValue } from 'app/shared/model/news.model';

export const ACTION_TYPES = {
  SEARCH_NEWS: 'news/SEARCH_NEWS',
  FETCH_NEWS_LIST: 'news/FETCH_NEWS_LIST',
  FETCH_NEWS: 'news/FETCH_NEWS',
  CREATE_NEWS: 'news/CREATE_NEWS',
  UPDATE_NEWS: 'news/UPDATE_NEWS',
  DELETE_NEWS: 'news/DELETE_NEWS',
  RESET: 'news/RESET'
};

const initialState = {
  loading: false,
  errorMessage: null,
  entities: [] as ReadonlyArray<INews>,
  entity: defaultValue,
  updating: false,
  totalItems: 0,
  updateSuccess: false
};

export type NewsState = Readonly<typeof initialState>;

// Reducer

export default (state: NewsState = initialState, action): NewsState => {
  switch (action.type) {
    case REQUEST(ACTION_TYPES.SEARCH_NEWS):
    case REQUEST(ACTION_TYPES.FETCH_NEWS_LIST):
    case REQUEST(ACTION_TYPES.FETCH_NEWS):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        loading: true
      };
    case REQUEST(ACTION_TYPES.CREATE_NEWS):
    case REQUEST(ACTION_TYPES.UPDATE_NEWS):
    case REQUEST(ACTION_TYPES.DELETE_NEWS):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        updating: true
      };
    case FAILURE(ACTION_TYPES.SEARCH_NEWS):
    case FAILURE(ACTION_TYPES.FETCH_NEWS_LIST):
    case FAILURE(ACTION_TYPES.FETCH_NEWS):
    case FAILURE(ACTION_TYPES.CREATE_NEWS):
    case FAILURE(ACTION_TYPES.UPDATE_NEWS):
    case FAILURE(ACTION_TYPES.DELETE_NEWS):
      return {
        ...state,
        loading: false,
        updating: false,
        updateSuccess: false,
        errorMessage: action.payload
      };
    case SUCCESS(ACTION_TYPES.SEARCH_NEWS):
      return {
        ...state,
        loading: false,
        entities: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.FETCH_NEWS_LIST):
      return {
        ...state,
        loading: false,
        totalItems: action.payload.headers['x-total-count'],
        entities: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.FETCH_NEWS):
      return {
        ...state,
        loading: false,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.CREATE_NEWS):
    case SUCCESS(ACTION_TYPES.UPDATE_NEWS):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.DELETE_NEWS):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        entity: {}
      };
    case ACTION_TYPES.RESET:
      return {
        ...initialState
      };
    default:
      return state;
  }
};

const apiUrl = 'api/news';
const apiSearchUrl = 'api/_search/news';

// Actions

export const getSearchEntities: ICrudSearchAction<INews> = query => ({
  type: ACTION_TYPES.SEARCH_NEWS,
  payload: axios.get<INews>(`${apiSearchUrl}?query=` + query)
});

export const getEntities: ICrudGetAllAction<INews> = (page, size, sort) => {
  const requestUrl = `${apiUrl}${sort ? `?page=${page}&size=${size}&sort=${sort}` : ''}`;
  return {
    type: ACTION_TYPES.FETCH_NEWS_LIST,
    payload: axios.get<INews>(`${requestUrl}${sort ? '&' : '?'}cacheBuster=${new Date().getTime()}`)
  };
};

export const getEntity: ICrudGetAction<INews> = id => {
  const requestUrl = `${apiUrl}/${id}`;
  return {
    type: ACTION_TYPES.FETCH_NEWS,
    payload: axios.get<INews>(requestUrl)
  };
};

export const createEntity: ICrudPutAction<INews> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.CREATE_NEWS,
    payload: axios.post(apiUrl, cleanEntity(entity))
  });
  dispatch(getEntities());
  return result;
};

export const updateEntity: ICrudPutAction<INews> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.UPDATE_NEWS,
    payload: axios.put(apiUrl, cleanEntity(entity))
  });
  dispatch(getEntities());
  return result;
};

export const deleteEntity: ICrudDeleteAction<INews> = id => async dispatch => {
  const requestUrl = `${apiUrl}/${id}`;
  const result = await dispatch({
    type: ACTION_TYPES.DELETE_NEWS,
    payload: axios.delete(requestUrl)
  });
  dispatch(getEntities());
  return result;
};

export const reset = () => ({
  type: ACTION_TYPES.RESET
});
