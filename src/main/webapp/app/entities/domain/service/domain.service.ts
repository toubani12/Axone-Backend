import { inject, Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IDomain, NewDomain } from '../domain.model';

export type PartialUpdateDomain = Partial<IDomain> & Pick<IDomain, 'id'>;

export type EntityResponseType = HttpResponse<IDomain>;
export type EntityArrayResponseType = HttpResponse<IDomain[]>;

@Injectable({ providedIn: 'root' })
export class DomainService {
  protected http = inject(HttpClient);
  protected applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/domains');

  create(domain: NewDomain): Observable<EntityResponseType> {
    return this.http.post<IDomain>(this.resourceUrl, domain, { observe: 'response' });
  }

  update(domain: IDomain): Observable<EntityResponseType> {
    return this.http.put<IDomain>(`${this.resourceUrl}/${this.getDomainIdentifier(domain)}`, domain, { observe: 'response' });
  }

  partialUpdate(domain: PartialUpdateDomain): Observable<EntityResponseType> {
    return this.http.patch<IDomain>(`${this.resourceUrl}/${this.getDomainIdentifier(domain)}`, domain, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IDomain>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IDomain[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getDomainIdentifier(domain: Pick<IDomain, 'id'>): number {
    return domain.id;
  }

  compareDomain(o1: Pick<IDomain, 'id'> | null, o2: Pick<IDomain, 'id'> | null): boolean {
    return o1 && o2 ? this.getDomainIdentifier(o1) === this.getDomainIdentifier(o2) : o1 === o2;
  }

  addDomainToCollectionIfMissing<Type extends Pick<IDomain, 'id'>>(
    domainCollection: Type[],
    ...domainsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const domains: Type[] = domainsToCheck.filter(isPresent);
    if (domains.length > 0) {
      const domainCollectionIdentifiers = domainCollection.map(domainItem => this.getDomainIdentifier(domainItem));
      const domainsToAdd = domains.filter(domainItem => {
        const domainIdentifier = this.getDomainIdentifier(domainItem);
        if (domainCollectionIdentifiers.includes(domainIdentifier)) {
          return false;
        }
        domainCollectionIdentifiers.push(domainIdentifier);
        return true;
      });
      return [...domainsToAdd, ...domainCollection];
    }
    return domainCollection;
  }
}
