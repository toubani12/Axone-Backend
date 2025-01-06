import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { ITemplate } from '../template.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../template.test-samples';

import { TemplateService } from './template.service';

const requireRestSample: ITemplate = {
  ...sampleWithRequiredData,
};

describe('Template Service', () => {
  let service: TemplateService;
  let httpMock: HttpTestingController;
  let expectedResult: ITemplate | ITemplate[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(TemplateService);
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

    it('should create a Template', () => {
      const template = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(template).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Template', () => {
      const template = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(template).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Template', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Template', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a Template', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addTemplateToCollectionIfMissing', () => {
      it('should add a Template to an empty array', () => {
        const template: ITemplate = sampleWithRequiredData;
        expectedResult = service.addTemplateToCollectionIfMissing([], template);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(template);
      });

      it('should not add a Template to an array that contains it', () => {
        const template: ITemplate = sampleWithRequiredData;
        const templateCollection: ITemplate[] = [
          {
            ...template,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addTemplateToCollectionIfMissing(templateCollection, template);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Template to an array that doesn't contain it", () => {
        const template: ITemplate = sampleWithRequiredData;
        const templateCollection: ITemplate[] = [sampleWithPartialData];
        expectedResult = service.addTemplateToCollectionIfMissing(templateCollection, template);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(template);
      });

      it('should add only unique Template to an array', () => {
        const templateArray: ITemplate[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const templateCollection: ITemplate[] = [sampleWithRequiredData];
        expectedResult = service.addTemplateToCollectionIfMissing(templateCollection, ...templateArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const template: ITemplate = sampleWithRequiredData;
        const template2: ITemplate = sampleWithPartialData;
        expectedResult = service.addTemplateToCollectionIfMissing([], template, template2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(template);
        expect(expectedResult).toContain(template2);
      });

      it('should accept null and undefined values', () => {
        const template: ITemplate = sampleWithRequiredData;
        expectedResult = service.addTemplateToCollectionIfMissing([], null, template, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(template);
      });

      it('should return initial array if no Template is added', () => {
        const templateCollection: ITemplate[] = [sampleWithRequiredData];
        expectedResult = service.addTemplateToCollectionIfMissing(templateCollection, undefined, null);
        expect(expectedResult).toEqual(templateCollection);
      });
    });

    describe('compareTemplate', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareTemplate(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareTemplate(entity1, entity2);
        const compareResult2 = service.compareTemplate(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareTemplate(entity1, entity2);
        const compareResult2 = service.compareTemplate(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareTemplate(entity1, entity2);
        const compareResult2 = service.compareTemplate(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
