import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, ActivatedRoute, Router, convertToParamMap } from '@angular/router';
import { of } from 'rxjs';

import { ITechCVEmployment } from '../tech-cv-employment.model';
import { TechCVEmploymentService } from '../service/tech-cv-employment.service';

import techCVEmploymentResolve from './tech-cv-employment-routing-resolve.service';

describe('TechCVEmployment routing resolve service', () => {
  let mockRouter: Router;
  let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
  let service: TechCVEmploymentService;
  let resultTechCVEmployment: ITechCVEmployment | null | undefined;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: {
            snapshot: {
              paramMap: convertToParamMap({}),
            },
          },
        },
      ],
    });
    mockRouter = TestBed.inject(Router);
    jest.spyOn(mockRouter, 'navigate').mockImplementation(() => Promise.resolve(true));
    mockActivatedRouteSnapshot = TestBed.inject(ActivatedRoute).snapshot;
    service = TestBed.inject(TechCVEmploymentService);
    resultTechCVEmployment = undefined;
  });

  describe('resolve', () => {
    it('should return ITechCVEmployment returned by find', () => {
      // GIVEN
      service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      TestBed.runInInjectionContext(() => {
        techCVEmploymentResolve(mockActivatedRouteSnapshot).subscribe({
          next(result) {
            resultTechCVEmployment = result;
          },
        });
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultTechCVEmployment).toEqual({ id: 123 });
    });

    it('should return null if id is not provided', () => {
      // GIVEN
      service.find = jest.fn();
      mockActivatedRouteSnapshot.params = {};

      // WHEN
      TestBed.runInInjectionContext(() => {
        techCVEmploymentResolve(mockActivatedRouteSnapshot).subscribe({
          next(result) {
            resultTechCVEmployment = result;
          },
        });
      });

      // THEN
      expect(service.find).not.toBeCalled();
      expect(resultTechCVEmployment).toEqual(null);
    });

    it('should route to 404 page if data not found in server', () => {
      // GIVEN
      jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse<ITechCVEmployment>({ body: null })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      TestBed.runInInjectionContext(() => {
        techCVEmploymentResolve(mockActivatedRouteSnapshot).subscribe({
          next(result) {
            resultTechCVEmployment = result;
          },
        });
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultTechCVEmployment).toEqual(undefined);
      expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
    });
  });
});
