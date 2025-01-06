import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IEmployer } from '../employer.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../employer.test-samples';

import { EmployerService } from './employer.service';

const requireRestSample: IEmployer = {
  ...sampleWithRequiredData,
};

describe('Employer Service', () => {
  let service: EmployerService;
  let httpMock: HttpTestingController;
  let expectedResult: IEmployer | IEmployer[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(EmployerService);
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

    it('should create a Employer', () => {
      const employer = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(employer).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Employer', () => {
      const employer = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(employer).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Employer', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Employer', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a Employer', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addEmployerToCollectionIfMissing', () => {
      it('should add a Employer to an empty array', () => {
        const employer: IEmployer = sampleWithRequiredData;
        expectedResult = service.addEmployerToCollectionIfMissing([], employer);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(employer);
      });

      it('should not add a Employer to an array that contains it', () => {
        const employer: IEmployer = sampleWithRequiredData;
        const employerCollection: IEmployer[] = [
          {
            ...employer,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addEmployerToCollectionIfMissing(employerCollection, employer);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Employer to an array that doesn't contain it", () => {
        const employer: IEmployer = sampleWithRequiredData;
        const employerCollection: IEmployer[] = [sampleWithPartialData];
        expectedResult = service.addEmployerToCollectionIfMissing(employerCollection, employer);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(employer);
      });

      it('should add only unique Employer to an array', () => {
        const employerArray: IEmployer[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const employerCollection: IEmployer[] = [sampleWithRequiredData];
        expectedResult = service.addEmployerToCollectionIfMissing(employerCollection, ...employerArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const employer: IEmployer = sampleWithRequiredData;
        const employer2: IEmployer = sampleWithPartialData;
        expectedResult = service.addEmployerToCollectionIfMissing([], employer, employer2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(employer);
        expect(expectedResult).toContain(employer2);
      });

      it('should accept null and undefined values', () => {
        const employer: IEmployer = sampleWithRequiredData;
        expectedResult = service.addEmployerToCollectionIfMissing([], null, employer, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(employer);
      });

      it('should return initial array if no Employer is added', () => {
        const employerCollection: IEmployer[] = [sampleWithRequiredData];
        expectedResult = service.addEmployerToCollectionIfMissing(employerCollection, undefined, null);
        expect(expectedResult).toEqual(employerCollection);
      });
    });

    describe('compareEmployer', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareEmployer(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareEmployer(entity1, entity2);
        const compareResult2 = service.compareEmployer(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareEmployer(entity1, entity2);
        const compareResult2 = service.compareEmployer(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareEmployer(entity1, entity2);
        const compareResult2 = service.compareEmployer(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
