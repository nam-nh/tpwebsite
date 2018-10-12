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

import { IItemGroup, defaultValue } from 'app/shared/model/item-group.model';

export const ACTION_TYPES = {
  SEARCH_ITEMGROUPS: 'itemGroup/SEARCH_ITEMGROUPS',
  FETCH_ITEMGROUP_LIST: 'itemGroup/FETCH_ITEMGROUP_LIST',
  FETCH_ITEMGROUP: 'itemGroup/FETCH_ITEMGROUP',
  CREATE_ITEMGROUP: 'itemGroup/CREATE_ITEMGROUP',
  UPDATE_ITEMGROUP: 'itemGroup/UPDATE_ITEMGROUP',
  DELETE_ITEMGROUP: 'itemGroup/DELETE_ITEMGROUP',
  RESET: 'itemGroup/RESET'
};

const initialState = {
  loading: false,
  errorMessage: null,
  entities: [] as ReadonlyArray<IItemGroup>,
  entity: defaultValue,
  links: { next: 0 },
  updating: false,
  totalItems: 0,
  updateSuccess: false
};

export type ItemGroupState = Readonly<typeof initialState>;

// Reducer

export default (state: ItemGroupState = initialState, action): ItemGroupState => {
  switch (action.type) {
    case REQUEST(ACTION_TYPES.SEARCH_ITEMGROUPS):
    case REQUEST(ACTION_TYPES.FETCH_ITEMGROUP_LIST):
    case REQUEST(ACTION_TYPES.FETCH_ITEMGROUP):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        loading: true
      };
    case REQUEST(ACTION_TYPES.CREATE_ITEMGROUP):
    case REQUEST(ACTION_TYPES.UPDATE_ITEMGROUP):
    case REQUEST(ACTION_TYPES.DELETE_ITEMGROUP):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        updating: true
      };
    case FAILURE(ACTION_TYPES.SEARCH_ITEMGROUPS):
    case FAILURE(ACTION_TYPES.FETCH_ITEMGROUP_LIST):
    case FAILURE(ACTION_TYPES.FETCH_ITEMGROUP):
    case FAILURE(ACTION_TYPES.CREATE_ITEMGROUP):
    case FAILURE(ACTION_TYPES.UPDATE_ITEMGROUP):
    case FAILURE(ACTION_TYPES.DELETE_ITEMGROUP):
      return {
        ...state,
        loading: false,
        updating: false,
        updateSuccess: false,
        errorMessage: action.payload
      };
    case SUCCESS(ACTION_TYPES.SEARCH_ITEMGROUPS):
      return {
        ...state,
        loading: false,
        entities: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.FETCH_ITEMGROUP_LIST):
      const links = parseHeaderForLinks(action.payload.headers.link);
      return {
        ...state,
        links,
        loading: false,
        totalItems: action.payload.headers['x-total-count'],
        entities: loadMoreDataWhenScrolled(state.entities, action.payload.data, links)
      };
    case SUCCESS(ACTION_TYPES.FETCH_ITEMGROUP):
      return {
        ...state,
        loading: false,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.CREATE_ITEMGROUP):
    case SUCCESS(ACTION_TYPES.UPDATE_ITEMGROUP):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.DELETE_ITEMGROUP):
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

const apiUrl = 'api/item-groups';
const apiSearchUrl = 'api/_search/item-groups';

// Actions

export const getSearchEntities: ICrudSearchAction<IItemGroup> = query => ({
  type: ACTION_TYPES.SEARCH_ITEMGROUPS,
  payload: axios.get<IItemGroup>(`${apiSearchUrl}?query=` + query)
});

export const getEntities: ICrudGetAllAction<IItemGroup> = (page, size, sort) => {
  const requestUrl = `${apiUrl}${sort ? `?page=${page}&size=${size}&sort=${sort}` : ''}`;
  return {
    type: ACTION_TYPES.FETCH_ITEMGROUP_LIST,
    payload: axios.get<IItemGroup>(`${requestUrl}${sort ? '&' : '?'}cacheBuster=${new Date().getTime()}`)
  };
};

export const getEntity: ICrudGetAction<IItemGroup> = id => {
  const requestUrl = `${apiUrl}/${id}`;
  return {
    type: ACTION_TYPES.FETCH_ITEMGROUP,
    payload: axios.get<IItemGroup>(requestUrl)
  };
};

export const createEntity: ICrudPutAction<IItemGroup> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.CREATE_ITEMGROUP,
    payload: axios.post(apiUrl, cleanEntity(entity))
  });
  return result;
};

export const updateEntity: ICrudPutAction<IItemGroup> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.UPDATE_ITEMGROUP,
    payload: axios.put(apiUrl, cleanEntity(entity))
  });
  return result;
};

export const deleteEntity: ICrudDeleteAction<IItemGroup> = id => async dispatch => {
  const requestUrl = `${apiUrl}/${id}`;
  const result = await dispatch({
    type: ACTION_TYPES.DELETE_ITEMGROUP,
    payload: axios.delete(requestUrl)
  });
  return result;
};

export const reset = () => ({
  type: ACTION_TYPES.RESET
});
