package com.service;

import com.filtro.FiltroArquivoAula;
import com.model.Arquivo;
import com.model.ArquivoAula;
import com.model.Aula;
import com.repository.ArquivoAulaRepository;
import com.repository.ArquivoRepository;
import com.repository.AulaRepository;
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
            s3Service.excluirArquivo(vinculo.getArquivo().getCaminho());
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
        s3Service.excluirArquivo(arquivo.getCaminho());
        arquivoRepository.delete(arquivo);
    }
    public List<ArquivoAula> getVincululosArquivo(Long idAula){
        var filtro = new FiltroArquivoAula();
        filtro.setIdAula(idAula);
        return arquivoAulaRepository.findAll(filtro.toSpecification());
    }
    public void setVinculoArquivo(List<ArquivoAula> vinculos, Long idAula){
        if(vinculos != null && !vinculos.isEmpty()){
            if(idAula != null){
                for(ArquivoAula arquivoAula : vinculos){
                    var newVinculo = new ArquivoAula();
                    var aula = aulaRepository.findById(idAula);
                    var arquivo = arquivoRepository.findById(arquivoAula.getArquivo().getId());

                    aula.ifPresent(newVinculo::setAula);
                    arquivo.ifPresent(arquivoAula::setArquivo);
                    arquivoAulaRepository.save(arquivoAula);
                }
            }
        }
    }
}
