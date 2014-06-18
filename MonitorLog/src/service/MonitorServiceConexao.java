/*	Este arquivo é parte do programa MonitorLog

    MonitorLog é um software livre; você pode redistribui-lo e/ou 
    modificá-lo dentro dos termos da Licença Pública Geral GNU como 
    publicada pela Fundação do Software Livre (FSF); na versão 2 da 
    Licença, ou qualquer versão.

    Este programa é distribuído na esperança que possa ser  útil, 
    mas SEM NENHUMA GARANTIA; sem uma garantia implícita de ADEQUAÇÂO a qualquer
    MERCADO ou APLICAÇÃO EM PARTICULAR. Veja a

    Licença Pública Geral GNU para maiores detalhes.
    Você deve ter recebido uma cópia da Licença Pública Geral GNU
    junto com este programa, se não, escreva para a Fundação do Software
    Livre(FSF) Inc., 51 Franklin St, Fifth Floor, Boston, MA  02110-1301  USA
    
 */
package service;

import service.widget.MonitorServiceInterface;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;

public class MonitorServiceConexao extends MonitorService implements
		MonitorServiceInterface {
	/**
	 * @see android.app.Service#onBind(Intent)
	 */
	private final IBinder conexao = new MonitorBinder();

	public class MonitorBinder extends Binder {
		public MonitorServiceInterface getService() {
			return MonitorServiceConexao.this;
		}
	}

	@Override
	public IBinder onBind(Intent intent) {
		return conexao;
	}
}
