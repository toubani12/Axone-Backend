import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IRecruiter } from '../recruiter.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../recruiter.test-samples';

import { RecruiterService } from './recruiter.service';

const requireRestSample: IRecruiter = {
  ...sampleWithRequiredData,
};

describe('Recruiter Service', () => {
  let service: RecruiterService;
  let httpMock: HttpTestingController;
  let expectedResult: IRecruiter | IRecruiter[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(RecruiterService);
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

    it('should create a Recruiter', () => {
      const recruiter = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(recruiter).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Recruiter', () => {
      const recruiter = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(recruiter).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Recruiter', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Recruiter', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a Recruiter', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addRecruiterToCollectionIfMissing', () => {
      it('should add a Recruiter to an empty array', () => {
        const recruiter: IRecruiter = sampleWithRequiredData;
        expectedResult = service.addRecruiterToCollectionIfMissing([], recruiter);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(recruiter);
      });

      it('should not add a Recruiter to an array that contains it', () => {
        const recruiter: IRecruiter = sampleWithRequiredData;
        const recruiterCollection: IRecruiter[] = [
          {
            ...recruiter,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addRecruiterToCollectionIfMissing(recruiterCollection, recruiter);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Recruiter to an array that doesn't contain it", () => {
        const recruiter: IRecruiter = sampleWithRequiredData;
        const recruiterCollection: IRecruiter[] = [sampleWithPartialData];
        expectedResult = service.addRecruiterToCollectionIfMissing(recruiterCollection, recruiter);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(recruiter);
      });

      it('should add only unique Recruiter to an array', () => {
        const recruiterArray: IRecruiter[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const recruiterCollection: IRecruiter[] = [sampleWithRequiredData];
        expectedResult = service.addRecruiterToCollectionIfMissing(recruiterCollection, ...recruiterArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const recruiter: IRecruiter = sampleWithRequiredData;
        const recruiter2: IRecruiter = sampleWithPartialData;
        expectedResult = service.addRecruiterToCollectionIfMissing([], recruiter, recruiter2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(recruiter);
        expect(expectedResult).toContain(recruiter2);
      });

      it('should accept null and undefined values', () => {
        const recruiter: IRecruiter = sampleWithRequiredData;
        expectedResult = service.addRecruiterToCollectionIfMissing([], null, recruiter, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(recruiter);
      });

      it('should return initial array if no Recruiter is added', () => {
        const recruiterCollection: IRecruiter[] = [sampleWithRequiredData];
        expectedResult = service.addRecruiterToCollectionIfMissing(recruiterCollection, undefined, null);
        expect(expectedResult).toEqual(recruiterCollection);
      });
    });

    describe('compareRecruiter', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareRecruiter(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareRecruiter(entity1, entity2);
        const compareResult2 = service.compareRecruiter(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareRecruiter(entity1, entity2);
        const compareResult2 = service.compareRecruiter(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareRecruiter(entity1, entity2);
        const compareResult2 = service.compareRecruiter(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
