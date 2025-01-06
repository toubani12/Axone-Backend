import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { DATE_FORMAT } from 'app/config/input.constants';
import { IApplication } from '../application.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../application.test-samples';

import { ApplicationService, RestApplication } from './application.service';

const requireRestSample: RestApplication = {
  ...sampleWithRequiredData,
  deadline: sampleWithRequiredData.deadline?.format(DATE_FORMAT),
  createdAt: sampleWithRequiredData.createdAt?.toJSON(),
  openedAt: sampleWithRequiredData.openedAt?.toJSON(),
  closedAt: sampleWithRequiredData.closedAt?.toJSON(),
  doneAt: sampleWithRequiredData.doneAt?.toJSON(),
};

describe('Application Service', () => {
  let service: ApplicationService;
  let httpMock: HttpTestingController;
  let expectedResult: IApplication | IApplication[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(ApplicationService);
    httpMock = TestBed.inject(HttpTestingController);
  });

  describe('Service methods', () => {
    it('should find an element', () => {
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.find(123).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should create a Application', () => {
      const application = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(application).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Application', () => {
      const application = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(application).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Application', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Application', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a Application', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addApplicationToCollectionIfMissing', () => {
      it('should add a Application to an empty array', () => {
        const application: IApplication = sampleWithRequiredData;
        expectedResult = service.addApplicationToCollectionIfMissing([], application);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(application);
      });

      it('should not add a Application to an array that contains it', () => {
        const application: IApplication = sampleWithRequiredData;
        const applicationCollection: IApplication[] = [
          {
            ...application,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addApplicationToCollectionIfMissing(applicationCollection, application);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Application to an array that doesn't contain it", () => {
        const application: IApplication = sampleWithRequiredData;
        const applicationCollection: IApplication[] = [sampleWithPartialData];
        expectedResult = service.addApplicationToCollectionIfMissing(applicationCollection, application);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(application);
      });

      it('should add only unique Application to an array', () => {
        const applicationArray: IApplication[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const applicationCollection: IApplication[] = [sampleWithRequiredData];
        expectedResult = service.addApplicationToCollectionIfMissing(applicationCollection, ...applicationArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const application: IApplication = sampleWithRequiredData;
        const application2: IApplication = sampleWithPartialData;
        expectedResult = service.addApplicationToCollectionIfMissing([], application, application2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(application);
        expect(expectedResult).toContain(application2);
      });

      it('should accept null and undefined values', () => {
        const application: IApplication = sampleWithRequiredData;
        expectedResult = service.addApplicationToCollectionIfMissing([], null, application, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(application);
      });

      it('should return initial array if no Application is added', () => {
        const applicationCollection: IApplication[] = [sampleWithRequiredData];
        expectedResult = service.addApplicationToCollectionIfMissing(applicationCollection, undefined, null);
        expect(expectedResult).toEqual(applicationCollection);
      });
    });

    describe('compareApplication', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareApplication(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareApplication(entity1, entity2);
        const compareResult2 = service.compareApplication(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareApplication(entity1, entity2);
        const compareResult2 = service.compareApplication(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareApplication(entity1, entity2);
        const compareResult2 = service.compareApplication(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
