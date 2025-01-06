import { inject, Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { map, Observable } from 'rxjs';

import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { DATE_FORMAT } from 'app/config/input.constants';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { INDA, NewNDA } from '../nda.model';

export type PartialUpdateNDA = Partial<INDA> & Pick<INDA, 'id'>;

type RestOf<T extends INDA | NewNDA> = Omit<T, 'period'> & {
  period?: string | null;
};

export type RestNDA = RestOf<INDA>;

export type NewRestNDA = RestOf<NewNDA>;

export type PartialUpdateRestNDA = RestOf<PartialUpdateNDA>;

export type EntityResponseType = HttpResponse<INDA>;
export type EntityArrayResponseType = HttpResponse<INDA[]>;

@Injectable({ providedIn: 'root' })
export class NDAService {
  protected http = inject(HttpClient);
  protected applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/ndas');

  create(nDA: NewNDA): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(nDA);
    return this.http.post<RestNDA>(this.resourceUrl, copy, { observe: 'response' }).pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(nDA: INDA): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(nDA);
    return this.http
      .put<RestNDA>(`${this.resourceUrl}/${this.getNDAIdentifier(nDA)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(nDA: PartialUpdateNDA): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(nDA);
    return this.http
      .patch<RestNDA>(`${this.resourceUrl}/${this.getNDAIdentifier(nDA)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<RestNDA>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestNDA[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getNDAIdentifier(nDA: Pick<INDA, 'id'>): number {
    return nDA.id;
  }

  compareNDA(o1: Pick<INDA, 'id'> | null, o2: Pick<INDA, 'id'> | null): boolean {
    return o1 && o2 ? this.getNDAIdentifier(o1) === this.getNDAIdentifier(o2) : o1 === o2;
  }

  addNDAToCollectionIfMissing<Type extends Pick<INDA, 'id'>>(nDACollection: Type[], ...nDASToCheck: (Type | null | undefined)[]): Type[] {
    const nDAS: Type[] = nDASToCheck.filter(isPresent);
    if (nDAS.length > 0) {
      const nDACollectionIdentifiers = nDACollection.map(nDAItem => this.getNDAIdentifier(nDAItem));
      const nDASToAdd = nDAS.filter(nDAItem => {
        const nDAIdentifier = this.getNDAIdentifier(nDAItem);
        if (nDACollectionIdentifiers.includes(nDAIdentifier)) {
          return false;
        }
        nDACollectionIdentifiers.push(nDAIdentifier);
        return true;
      });
      return [...nDASToAdd, ...nDACollection];
    }
    return nDACollection;
  }

  protected convertDateFromClient<T extends INDA | NewNDA | PartialUpdateNDA>(nDA: T): RestOf<T> {
    return {
      ...nDA,
      period: nDA.period?.format(DATE_FORMAT) ?? null,
    };
  }

  protected convertDateFromServer(restNDA: RestNDA): INDA {
    return {
      ...restNDA,
      period: restNDA.period ? dayjs(restNDA.period) : undefined,
    };
  }

  protected convertResponseFromServer(res: HttpResponse<RestNDA>): HttpResponse<INDA> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(res: HttpResponse<RestNDA[]>): HttpResponse<INDA[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }
}
