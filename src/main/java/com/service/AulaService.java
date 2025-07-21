package com.service;

import com.filtro.FiltroArquivoAula;
import com.model.*;
import com.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AulaService {
    @Autowired
    private AulaRepository aulaRepository;

    @Autowired
    private ArquivoRepository arquivoRepository;

    @Autowired
    private ArquivoAulaRepository arquivoAulaRepository;

    @Autowired
    private S3Service s3Service;

    public void removerVinculosPorAula(Aula aula){
        var filtro = new FiltroArquivoAula();
        filtro.setIdAula(aula.getId());
        var vinculados = arquivoAulaRepository.findAll(filtro.toSpecification());

        for(ArquivoAula vinculo : vinculados){
            s3Service.deleteFileFromS3(vinculo.getArquivo().getCaminho());
            arquivoAulaRepository.delete(vinculo);
            arquivoRepository.delete(vinculo.getArquivo());
        }
    }
    public void removerVinculosPorArquivo(Arquivo arquivo){
        var filtro = new FiltroArquivoAula();
        filtro.setIdArquivo(arquivo.getId());

        var arquivosAulas = arquivoAulaRepository.findAll(filtro.toSpecification());
        for(ArquivoAula arquivoAula : arquivosAulas){
            arquivoAulaRepository.delete(arquivoAula);
        }
        s3Service.deleteFileFromS3(arquivo.getCaminho());
        arquivoRepository.delete(arquivo);
    }
}
