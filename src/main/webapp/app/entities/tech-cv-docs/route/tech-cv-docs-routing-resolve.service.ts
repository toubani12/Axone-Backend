import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of, EMPTY, Observable } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ITechCVDocs } from '../tech-cv-docs.model';
import { TechCVDocsService } from '../service/tech-cv-docs.service';

const techCVDocsResolve = (route: ActivatedRouteSnapshot): Observable<null | ITechCVDocs> => {
  const id = route.params['id'];
  if (id) {
    return inject(TechCVDocsService)
      .find(id)
      .pipe(
        mergeMap((techCVDocs: HttpResponse<ITechCVDocs>) => {
          if (techCVDocs.body) {
            return of(techCVDocs.body);
          } else {
            inject(Router).navigate(['404']);
            return EMPTY;
          }
        }),
      );
  }
  return of(null);
};

export default techCVDocsResolve;
