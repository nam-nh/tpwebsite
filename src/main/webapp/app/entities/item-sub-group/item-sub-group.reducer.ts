import axios from 'axios';
import {
  ICrudSearchAction,
  parseHeaderForLinks,
  loadMoreDataWhenScrolled,
  ICrudGetAction,
  ICrudGetAllAction,
  ICrudPutAction,
  ICrudDeleteAction
} from 'react-jhipster';

import { cleanEntity } from 'app/shared/util/entity-utils';
import { REQUEST, SUCCESS, FAILURE } from 'app/shared/reducers/action-type.util';

import { IItemSubGroup, defaultValue } from 'app/shared/model/item-sub-group.model';

export const ACTION_TYPES = {
  SEARCH_ITEMSUBGROUPS: 'itemSubGroup/SEARCH_ITEMSUBGROUPS',
  FETCH_ITEMSUBGROUP_LIST: 'itemSubGroup/FETCH_ITEMSUBGROUP_LIST',
  FETCH_ITEMSUBGROUP: 'itemSubGroup/FETCH_ITEMSUBGROUP',
  CREATE_ITEMSUBGROUP: 'itemSubGroup/CREATE_ITEMSUBGROUP',
  UPDATE_ITEMSUBGROUP: 'itemSubGroup/UPDATE_ITEMSUBGROUP',
  DELETE_ITEMSUBGROUP: 'itemSubGroup/DELETE_ITEMSUBGROUP',
  RESET: 'itemSubGroup/RESET'
};

const initialState = {
  loading: false,
  errorMessage: null,
  entities: [] as ReadonlyArray<IItemSubGroup>,
  entity: defaultValue,
  links: { next: 0 },
  updating: false,
  totalItems: 0,
  updateSuccess: false
};

export type ItemSubGroupState = Readonly<typeof initialState>;

// Reducer

export default (state: ItemSubGroupState = initialState, action): ItemSubGroupState => {
  switch (action.type) {
    case REQUEST(ACTION_TYPES.SEARCH_ITEMSUBGROUPS):
    case REQUEST(ACTION_TYPES.FETCH_ITEMSUBGROUP_LIST):
    case REQUEST(ACTION_TYPES.FETCH_ITEMSUBGROUP):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        loading: true
      };
    case REQUEST(ACTION_TYPES.CREATE_ITEMSUBGROUP):
    case REQUEST(ACTION_TYPES.UPDATE_ITEMSUBGROUP):
    case REQUEST(ACTION_TYPES.DELETE_ITEMSUBGROUP):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        updating: true
      };
    case FAILURE(ACTION_TYPES.SEARCH_ITEMSUBGROUPS):
    case FAILURE(ACTION_TYPES.FETCH_ITEMSUBGROUP_LIST):
    case FAILURE(ACTION_TYPES.FETCH_ITEMSUBGROUP):
    case FAILURE(ACTION_TYPES.CREATE_ITEMSUBGROUP):
    case FAILURE(ACTION_TYPES.UPDATE_ITEMSUBGROUP):
    case FAILURE(ACTION_TYPES.DELETE_ITEMSUBGROUP):
      return {
        ...state,
        loading: false,
        updating: false,
        updateSuccess: false,
        errorMessage: action.payload
      };
    case SUCCESS(ACTION_TYPES.SEARCH_ITEMSUBGROUPS):
      return {
        ...state,
        loading: false,
        entities: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.FETCH_ITEMSUBGROUP_LIST):
      const links = parseHeaderForLinks(action.payload.headers.link);
      return {
        ...state,
        links,
        loading: false,
        totalItems: action.payload.headers['x-total-count'],
        entities: loadMoreDataWhenScrolled(state.entities, action.payload.data, links)
      };
    case SUCCESS(ACTION_TYPES.FETCH_ITEMSUBGROUP):
      return {
        ...state,
        loading: false,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.CREATE_ITEMSUBGROUP):
    case SUCCESS(ACTION_TYPES.UPDATE_ITEMSUBGROUP):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.DELETE_ITEMSUBGROUP):
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

const apiUrl = 'api/item-sub-groups';
const apiSearchUrl = 'api/_search/item-sub-groups';

// Actions

export const getSearchEntities: ICrudSearchAction<IItemSubGroup> = query => ({
  type: ACTION_TYPES.SEARCH_ITEMSUBGROUPS,
  payload: axios.get<IItemSubGroup>(`${apiSearchUrl}?query=` + query)
});

export const getEntities: ICrudGetAllAction<IItemSubGroup> = (page, size, sort) => {
  const requestUrl = `${apiUrl}${sort ? `?page=${page}&size=${size}&sort=${sort}` : ''}`;
  return {
    type: ACTION_TYPES.FETCH_ITEMSUBGROUP_LIST,
    payload: axios.get<IItemSubGroup>(`${requestUrl}${sort ? '&' : '?'}cacheBuster=${new Date().getTime()}`)
  };
};

export const getEntity: ICrudGetAction<IItemSubGroup> = id => {
  const requestUrl = `${apiUrl}/${id}`;
  return {
    type: ACTION_TYPES.FETCH_ITEMSUBGROUP,
    payload: axios.get<IItemSubGroup>(requestUrl)
  };
};

export const createEntity: ICrudPutAction<IItemSubGroup> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.CREATE_ITEMSUBGROUP,
    payload: axios.post(apiUrl, cleanEntity(entity))
  });
  return result;
};

export const updateEntity: ICrudPutAction<IItemSubGroup> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.UPDATE_ITEMSUBGROUP,
    payload: axios.put(apiUrl, cleanEntity(entity))
  });
  return result;
};

export const deleteEntity: ICrudDeleteAction<IItemSubGroup> = id => async dispatch => {
  const requestUrl = `${apiUrl}/${id}`;
  const result = await dispatch({
    type: ACTION_TYPES.DELETE_ITEMSUBGROUP,
    payload: axios.delete(requestUrl)
  });
  return result;
};

export const reset = () => ({
  type: ACTION_TYPES.RESET
});
